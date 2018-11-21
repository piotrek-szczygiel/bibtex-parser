package com.szczygiel.bibtex;

import java.util.List;

public class Document {
    private List<Entry> entries;

    Document(String filePath) {
        String fileContent = File.read(filePath);

        Parser parser = new Parser();
        entries = parser.parse(fileContent);

        Strings strings = new Strings();
        strings.extractFrom(entries);

        for (Entry entry : entries) {
            entry.computeStrings(strings);
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Entry entry : entries) {
            if (entry.getEntryType().equals("string")) {
                continue;
            }

            str.append("-------------------\n");
            str.append(entry);
            str.append("\n");
        }

        return str.toString();
    }
}
