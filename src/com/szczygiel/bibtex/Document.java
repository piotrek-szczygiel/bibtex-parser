package com.szczygiel.bibtex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Stores whole BiBteX document as list of {@link Entry entries}.
 * <p>
 * Provides {@link Filter filtering} and {@link PrettyFormat printing ASCII tables}.
 */
public class Document {
    /**
     * List of {@link Entry entries} contained in a document.
     */
    private List<Entry> entries;

    /**
     * Contents of a BiBteX file as a {@link String}.
     */
    private String fileContents;


    /**
     * Get {@link #entries}.
     *
     * @return entries
     */
    List<Entry> getEntries() {
        return entries;
    }

    /**
     * Load BiBteX document from {@link File}.
     *
     * @param file BiBteX {@link File}.
     * @return success
     */
    boolean load(File file) {
        try {
            fileContents = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            System.out.println("unable to open file: " + file.getAbsolutePath());
            return false;
        }

        return true;
    }

    /**
     * Parse BiBteX document.
     */
    void parse() {
        if (fileContents.equals("")) {
            return;
        }

        Parser parser = new Parser();
        entries = parser.parse(fileContents);

        Strings strings = new Strings();
        strings.extractFrom(entries);

        for (Entry entry : entries) {
            entry.computeStrings(strings);
            entry.computeConcatenation(strings);
        }

        // Cleanup UNKNOWN fields
        for (Entry entry : entries) {
            ListIterator<Field> fieldsIterator = entry.fieldsIterator();
            while (fieldsIterator.hasNext()) {
                if (fieldsIterator.next().getType() == Field.Type.UNKNOWN) {
                    fieldsIterator.remove();
                }
            }
        }

        validate();
        fillAuthors();
    }

    /**
     * Check if all {@link #entries} are valid according to {@link SpecificEntries}.
     * <p>
     * Remove ignored fields.
     * Throw exception on missing required fields.
     */
    private void validate() {
        List<Entry> correctEntries = new ArrayList<>();

        for (Entry entry : entries) {
            // Don't add entries of unspecified type
            SpecificEntries.SpecificEntry specificEntry = SpecificEntries.get(entry.getEntryType());
            if (specificEntry == null) {
                continue;
            }


            Entry correctEntry = new Entry();
            correctEntry.setEntryType(entry.getEntryType());
            correctEntry.setCitationKey(entry.getCitationKey());

            List<Field> fields = entry.getFields();

            // Check for required fields
            for (String requiredField : specificEntry.requiredFields) {
                if (requiredField.equals("")) {
                    continue;
                }

                String key = "";

                boolean found = false;

                boolean twoValues = false;
                String firstValue = "", secondValue = "";

                // If the required field can be one of two values
                if (requiredField.contains("|")) {
                    twoValues = true;
                    String[] parts = requiredField.split("\\|");
                    firstValue = parts[0];
                    secondValue = parts[1];
                }

                // If there are two possible values
                if (twoValues) {
                    for (Field field : fields) {
                        key = field.getKey();

                        // Check if current field is either one of them
                        if (key.equals(firstValue) || key.equals(secondValue)) {
                            found = true;
                            break;
                        }
                    }
                } else {
                    for (Field field : fields) {
                        key = field.getKey();

                        // Else if there is only one possible value try to match it
                        if (key.equals(requiredField)) {
                            found = true;
                            break;
                        }
                    }
                }

                if (found) {
                    correctEntry.addField(entry.getField(key));
                } else {  // If current entry didn't contain one of required entries - throw an exception
                    throw new IllegalArgumentException("Entry " + entry.getCitationKey() + ", does not contain " +
                            "required field: " + requiredField);
                }
            }

            // Add existing optional fields
            for (String optionalField : specificEntry.optionalFields) {
                for (Field field : fields) {
                    String key = field.getKey();

                    if (key.equals(optionalField)) {
                        correctEntry.addField(entry.getField(key));
                    }
                }
            }

            correctEntries.add(correctEntry);
        }

        entries = correctEntries;
    }

    /**
     * Fill {@link #entries} with its authors.
     */
    private void fillAuthors() {
        for (Entry entry : entries) {
            for (Field field : entry.getFields()) {
                if (field.getType() == Field.Type.STRING &&
                        (field.getKey().equals("author") || field.getKey().equals("editor"))) {
                    String value = (String) field.getValue();
                    String[] authors = value.split("\\|");
                    for (String author : authors) {
                        author = author.trim();

                        int lastSpace = author.lastIndexOf(" ");
                        String firstName = author.substring(0, lastSpace);
                        String lastName = author.substring(lastSpace + 1);

                        entry.addAuthor(firstName, lastName);
                    }
                }
            }
        }
    }

    /**
     * Convert this document into ASCII tables.
     *
     * @return ASCII tables
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Entry entry : entries) {
            if (entry.getEntryType().equals("string")) {
                continue;
            }

            str.append(entry);
            str.append("\n\n");
        }

        return str.toString();
    }
}
