package com.szczygiel.bibtex;

import java.util.List;

/**
 * Stores whole BiBteX document.
 * <p>
 * Provides various functionalities such as searching, etc.
 */
public class Document {
    private String fileContent;
    private List<Entry> entries;

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
            for (int i = fields.size() - 1; i >= 0; i--) {
                if (fields.get(i).getType() == Field.Type.UNKNOWN) {
                    fields.remove(i);
                }
            }
        }
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
