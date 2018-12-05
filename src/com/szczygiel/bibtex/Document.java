package com.szczygiel.bibtex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Stores whole BibTeX document as list of {@link Entry entries}.
 * <p>
 * Provides {@link Filter filtering} and {@link PrettyFormat printing ASCII tables}.
 */
public class Document {
    /**
     * List of {@link Entry entries} contained in a document.
     */
    private List<Entry> entries;

    /**
     * Contents of a BibTeX file as a {@link String}.
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
     * Load BibTeX document from {@link File}.
     *
     * @param file BibTeX {@link File}.
     * @return success
     */
    boolean loadFile(File file) {
        try {
            fileContents = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            System.err.println("unable to open file: " + file.getAbsolutePath());
            return false;
        }

        return true;
    }

    /**
     * Set {@link #fileContents}.
     *
     * @param fileContents file contents
     */
    void loadString(String fileContents) {
        this.fileContents = fileContents;
    }

    /**
     * Parse BibTeX document.
     */
    void parse() {
        if (fileContents.equals("")) {
            return;
        }

        entries = SingletonParser.getInstance().parseDocument(fileContents);

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

        checkDuplicates();
        fillCrossreferences();
        validate();
        fillAuthors();
    }

    /**
     * Check for duplicates - entries having same citation key.
     */
    private void checkDuplicates() {
        for (Entry entry : entries) {
            SingletonSpecificEntries specificEntries = SingletonSpecificEntries.getInstance();
            SingletonSpecificEntries.SpecificEntry specificEntry = specificEntries.get(entry.getEntryType());
            if (specificEntry == null) {
                continue;
            }

            String citationKey = entry.getCitationKey();
            for (Entry otherEntry : entries) {
                // Don't compare entry to itself
                if (entry == otherEntry) {
                    continue;
                }

                String otherCitationKey = otherEntry.getCitationKey();
                if (citationKey.equals(otherCitationKey)) {
                    throw new IllegalArgumentException("Entry redefinition in line " +
                            otherEntry.getLineNumber() + ": " + citationKey);
                }
            }
        }
    }

    /**
     * Fill fields from crossreferenced entries.
     */
    private void fillCrossreferences() {
        for (Entry entry : entries) {
            boolean crossreferences = false;
            Entry crossrefEntry = null;

            for (Field field : entry.getFields()) {
                if (field.getKey().equals("crossref") && field.getType() == Field.Type.STRING) {
                    crossreferences = true;
                    String crossref = (String) field.getValue();
                    crossref = crossref.toLowerCase();

                    // Entry crossreferencing itself
                    if (crossref.equals(entry.getCitationKey())) {
                        throw new IllegalArgumentException("Entry self crossreference in line " +
                                entry.getLineNumber() + ": " + entry.getCitationKey());
                    }

                    for (Entry crossrefEntrySearch : entries) {
                        if (crossrefEntrySearch.getCitationKey().equals(crossref)) {
                            crossrefEntry = crossrefEntrySearch;
                        }
                    }

                    if (crossrefEntry == null) {
                        throw new IllegalArgumentException("Unknown crossreference in line " +
                                entry.getLineNumber() + ": " + crossref);
                    }
                }
            }

            if (crossreferences) {
                for (Field crossrefField : crossrefEntry.getFields()) {
                    boolean found = false;

                    // Find if crossreferencing entry already implements the field
                    for (Field field : entry.getFields()) {
                        if (field.getKey().equals(crossrefField.getKey())) {
                            found = true;
                        }
                    }

                    // Add missing field
                    if (!found) {
                        entry.addField(crossrefField);
                    }
                }
            }
        }
    }

    /**
     * Check if all {@link #entries} are valid according to {@link SingletonSpecificEntries}.
     * <p>
     * Remove ignored fields.
     * Throw exception on missing required fields.
     */
    private void validate() {
        List<Entry> correctEntries = new ArrayList<>();

        for (Entry entry : entries) {
            SingletonSpecificEntries specificEntries = SingletonSpecificEntries.getInstance();
            SingletonSpecificEntries.SpecificEntry specificEntry = specificEntries.get(entry.getEntryType());
            if (specificEntry == null) {
                if (entry.getEntryType().equals("string") || entry.getEntryType().equals("crossref")) {
                    continue;
                }

                throw new IllegalArgumentException("invalid entry type in line " +
                        entry.getLineNumber() + ": " + entry.getEntryType());
            }


            Entry correctEntry = new Entry();
            correctEntry.setEntryType(entry.getEntryType());
            correctEntry.setCitationKey(entry.getCitationKey());

            List<Field> fields = entry.getFields();

            for (Field field : fields) {
                if (field.getKey().equals("crossref")) {
                    correctEntry.addField(field);
                }
            }

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
                    throw new IllegalArgumentException("Entry " + entry.getCitationKey() + " in line " +
                            entry.getLineNumber() + ", doesn't contain required field: " + requiredField);
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
            entry.fillAuthors();
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
            str.append(entry);
            str.append("\n\n");
        }

        return str.toString();
    }
}
