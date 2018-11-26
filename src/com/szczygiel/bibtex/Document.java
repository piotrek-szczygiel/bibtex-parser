package com.szczygiel.bibtex;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores whole BiBteX document.
 * <p>
 * Provides various functionalities such as searching, etc.
 */
public class Document {
    private String fileContent;
    private List<Entry> entries;

    Document() {
        SpecificEntries.populate();
    }

    /**
     * Loads BiBteX document from file.
     *
     * @param filePath path to BiBteX file
     * @return success
     */
    boolean load(String filePath) {
        fileContent = File.read(filePath);

        if (fileContent.equals("")) {
            System.out.println("unable to read from file: " + filePath);
            return false;
        }

        return true;
    }

    /**
     * Parses BiBteX document.
     */
    void parse() {
        if (fileContent.equals("")) {
            return;
        }

        Parser parser = new Parser();
        entries = parser.parse(fileContent);

        Strings strings = new Strings();
        strings.extractFrom(entries);

        for (Entry entry : entries) {
            entry.computeStrings(strings);
            entry.computeConcatenation(strings);
        }

        // Cleanup UNKNOWN fields
        for (Entry entry : entries) {
            List<Field> fields = entry.getFields();

            // Iterating like this allows easy deletion of elements
            for (int i = fields.size() - 1; i >= 0; i--) {
                if (fields.get(i).getType() == Field.Type.UNKNOWN) {
                    fields.remove(i);
                }
            }
        }

        validate();
    }

    /**
     * Checks if all entries all valid according to SpecificEntries
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

                boolean two_values = false;
                String first_value = "", second_value = "";

                // If the required field can be one of two values
                if (requiredField.contains("|")) {
                    two_values = true;
                    String[] parts = requiredField.split("\\|");
                    first_value = parts[0];
                    second_value = parts[1];
                }

                // If there are two possible values
                if (two_values) {
                    for (Field field : fields) {
                        key = field.getKey();

                        // Check if current field is either one of them
                        if (key.equals(first_value) || key.equals(second_value)) {
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
