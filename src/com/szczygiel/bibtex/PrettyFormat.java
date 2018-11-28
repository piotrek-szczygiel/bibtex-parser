package com.szczygiel.bibtex;

import static java.lang.Math.max;

/**
 * ASCII art table formatter for BiBteX entries.
 */
class PrettyFormat {

    /**
     * Convert entry into an ASCII table.
     *
     * @param entry entry to format
     * @return ASCII table
     */
    static String table(Entry entry) {
        StringBuilder str = new StringBuilder();

        int keyWidth = 0;
        int valueWidth = 0;

        // Determine length of columns
        for (Field field : entry.getFields()) {
            String fieldKey = field.getKey();
            String fieldValue = String.valueOf(field.getValue());

            keyWidth = max(keyWidth, fieldKey.length());

            if (fieldKey.equals("author") || fieldKey.equals("editor")) {
                String[] authors = fieldValue.split("\\|");
                for (int i = 0; i < authors.length; i++) {
                    authors[i] = "* " + authors[i].trim();
                    valueWidth = max(valueWidth, authors[i].length());
                }
            } else if (fieldValue.contains("\n") || fieldValue.contains("\r")) {
                String[] lines = fieldValue.split("\\r\\n|\\r|\\n");
                for (String line : lines) {
                    valueWidth = max(valueWidth, line.length());
                }
            } else {
                valueWidth = max(valueWidth, fieldValue.length());
            }
        }

        // Padding
        keyWidth += 2;
        valueWidth += 2;


        // Header
        // ╔══════╤════════╗
        str.append("╔");
        str.append(repeat("═", keyWidth + valueWidth + 1));
        str.append("╗\n");

        String formatHeader = String.format("║ %%-%ds║\n", keyWidth + valueWidth);
        str.append(String.format(formatHeader, String.format("%s (%s)", entry.getEntryType().toUpperCase(),
                entry.getCitationKey())));

        // Header/Fields separator
        // ╠══════╤════════╣
        str.append("╠");
        str.append(repeat("═", keyWidth));
        str.append("╤");
        str.append(repeat("═", valueWidth));
        str.append("╣\n");

        // Formatters for keys and values
        String formatKey = String.format("║ %%-%ds", keyWidth - 1);
        String formatValue = String.format("│ %%-%ds║\n", valueWidth - 1);

        // Fields
        for (int i = 0; i < entry.getFields().size(); i++) {
            Field field = entry.getFields().get(i);
            String fieldKey = field.getKey();
            String fieldValue = String.valueOf(field.getValue());

            str.append(String.format(formatKey, fieldKey));

            if (fieldKey.equals("author") || fieldKey.equals("editor")) {
                String[] authors = fieldValue.split("\\|");
                for (int j = 0; j < authors.length; j++) {
                    authors[j] = "• " + authors[j].trim();
                }
                str.append(formatMultilineTableValue(authors, formatValue, keyWidth));
            } else if (fieldValue.contains("\n") || fieldValue.contains("\r")) {
                String[] lines = fieldValue.split("\\r\\n|\\r|\\n");
                str.append(formatMultilineTableValue(lines, formatValue, keyWidth));
            } else {
                str.append(String.format(formatValue, fieldValue));
            }

            if (i + 1 != entry.getFields().size()) {
                // Row separator
                // ╟──────┼────────╢
                str.append("╟");
                str.append(repeat("─", keyWidth));
                str.append("┼");
                str.append(repeat("─", valueWidth));
                str.append("╢\n");
            } else {
                // After last row
                // ╚══════╧════════╝
                str.append("╚");
                str.append(repeat("═", keyWidth));
                str.append("╧");
                str.append(repeat("═", valueWidth));
                str.append("╝");
            }

        }

        return str.toString();
    }

    /**
     * Repeat given string n times.
     *
     * @param string string to repeat
     * @param n      how many times
     * @return repeated string
     */
    private static String repeat(String string, int n) {
        return new String(new char[n]).replace("\0", string);
    }

    /**
     * Format multiline value, so it looks pretty when in table.
     *
     * @param lines   array of lines to format
     * @param format  format string
     * @param padding number of spaces to add before each line
     * @return formatted string
     */
    private static String formatMultilineTableValue(String[] lines, String format, int padding) {
        StringBuilder str = new StringBuilder();
        for (int j = 0; j < lines.length; j++) {
            str.append(String.format(format, lines[j]));

            if (j + 1 < lines.length) {
                str.append("║");
                str.append(repeat(" ", padding));
            }
        }

        return str.toString();
    }
}
