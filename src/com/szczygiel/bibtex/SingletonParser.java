package com.szczygiel.bibtex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Parses BibTeX input.
 * <p>
 * Uses singleton design pattern.
 */
class SingletonParser {
    /**
     * Stores ending index of last {@link #cutEntry} result.
     */
    private int lastEndingIndex = 0;

    /**
     * Prevents creating new instance of this class.
     */
    private SingletonParser() {

    }

    /**
     * Return instance of this singleton class.
     *
     * @return instance of {@link SingletonParser}
     */
    static SingletonParser getInstance() {
        return ParserHolder.INSTANCE;
    }

    /**
     * Parses BibTeX input into list of {@link Entry entries}.
     *
     * @param input BibTeX input
     * @return list of {@link Entry entries}
     */
    List<Entry> parseDocument(String input) {
        List<Entry> entries = new ArrayList<>();

        Matcher entryBeginningMatcher = SingletonPatterns.getInstance().matchEntryBeginning(input);
        while (entryBeginningMatcher.find()) {
            int index = entryBeginningMatcher.start();
            int line = getLineNumber(input, index);

            String entryStr = cutEntry(input, index);
            if (entryStr == null) {
                continue;
            }

            Entry entry = parseEntry(entryStr);
            if (entry != null) {
                entry.setLineNumber(line);
                entries.add(entry);
            }
        }

        lastEndingIndex = 0;
        return entries;
    }

    /**
     * Get line number from byte index.
     *
     * @param input input multiline string
     * @param index index of the cursor position
     * @return line number
     */
    private int getLineNumber(String input, int index) {
        int line = 1;
        for (int i = 0; i < index; i++) {
            char c = input.charAt(i);
            if (c == '\r') {
                line++;
                if (i + 1 < index && input.charAt(i + 1) == '\n') {
                    i++;
                }
            } else if (c == '\n') {
                line++;
            }
        }

        return line;
    }

    /**
     * Cuts an entry until last bracket.
     * <p>
     * Allows for nested brackets.
     *
     * @param input           BibTeX input
     * @param matchStartIndex index at which the entry begins
     * @return single BibTeX entry as {@link String}
     */
    String cutEntry(String input, int matchStartIndex) {
        if (matchStartIndex < lastEndingIndex) {
            System.out.println("nested entries at byte: " + matchStartIndex);
            return null;
        }

        StringBuilder entryStr = new StringBuilder();
        int bracketLevel = 0;
        for (int i = matchStartIndex; i < input.length(); i++) {
            char c = input.charAt(i);
            entryStr.append(c);
            if (c == '{') {
                bracketLevel++;
            } else if (c == '}') {
                bracketLevel--;

                if (bracketLevel == 0) {
                    lastEndingIndex = i;
                    break;
                }
            }
        }

        return entryStr.toString();
    }

    /**
     * Parses BibTeX entry into an {@link Entry} object.
     * <p>
     * Ignores @PREAMBLE and @COMMENT entries.
     *
     * @param entryStr entry to parse
     * @return parsed {@link Entry}
     */
    Entry parseEntry(String entryStr) {
        entryStr = entryStr.strip();
        Matcher entryMatcher = SingletonPatterns.getInstance().matchEntry(entryStr);

        if (!entryMatcher.find() || entryMatcher.groupCount() < 2) {
            return null;
        }

        Entry entry = new Entry();

        String entryType = entryMatcher.group(1);
        entryType = entryType.toLowerCase();
        entry.setEntryType(entryType);

        if (entryType.equals("preamble") || entryType.equals("comment")) {
            return null;
        }

        if (entryMatcher.groupCount() < 4) {
            System.out.println("invalid group count: " + entryMatcher.groupCount()
                    + "for entry: " + entryMatcher.group(0));
            return null;
        }

        String citationKey = entryMatcher.group(2);
        if (citationKey != null) {
            entry.setCitationKey(citationKey);
        } else if (!entryType.equals("string")) { // Everything but @String need citation key
            System.out.println("entry without citation key: " + entryMatcher.group(0));
            return null;
        }

        String keyValueStructure = entryMatcher.group(3);
        if (keyValueStructure == null) {
            System.out.println("entry without key value structure: " + entryMatcher.group(0));
            return null;
        }
        Matcher fieldMatcher = SingletonPatterns.getInstance().matchField(keyValueStructure);

        // Find key value combination
        while (fieldMatcher.find()) {
            if (fieldMatcher.groupCount() < 1) {
                continue;
            }
            String fieldStr = fieldMatcher.group(1);
            Field field = parseField(fieldStr);
            if (field == null) {
                continue;
            }

            if (field.getType() == Field.Type.UNKNOWN) {
                String raw = field.getRaw();
                raw = raw.replaceAll("\\r\\n|\\r|\\n", "\n\t");
                System.out.println("cannot parse field in entry " + entry.getCitationKey() + ":\n\t" + raw);
                continue;
            }

            entry.addField(field);
        }

        return entry;
    }

    /**
     * Parses BibTeX field into a {@link Field} object.
     *
     * @param input BibTeX field
     * @return parsed {@link Field}
     */
    Field parseField(String input) {
        input = input.strip();
        if (input == null || input.equals("")) {
            return null;
        }

        Field field = new Field();
        field.setRaw(input);

        String key;
        Object value;
        Field.Type type;

        SingletonPatterns patterns = SingletonPatterns.getInstance();

        Matcher stringMatcher = patterns.matchString(input);
        Matcher numberMatcher = patterns.matchNumber(input);
        Matcher referenceMatcher = patterns.matchReference(input);
        Matcher concatenationMatcher = patterns.matchConcatenation(input);

        // Match key value pair to string, number or reference
        Matcher actualMatcher;
        if (stringMatcher.find()) {
            actualMatcher = stringMatcher;
        } else if (numberMatcher.find()) {
            actualMatcher = numberMatcher;
        } else if (referenceMatcher.find()) {
            actualMatcher = referenceMatcher;
        } else if (concatenationMatcher.find()) {
            actualMatcher = concatenationMatcher;
        } else {  // Return empty field with only raw value
            return field;
        }

        if (actualMatcher.groupCount() < 2) {
            return field;
        }

        key = actualMatcher.group(1);
        key = key.toLowerCase();
        field.setKey(key);

        String valueStr = actualMatcher.group(2);

        if (actualMatcher == stringMatcher) {
            value = valueStr;
            type = Field.Type.STRING;
        } else if (actualMatcher == numberMatcher) {
            value = Integer.parseInt(valueStr);
            type = Field.Type.NUMBER;
        } else if (actualMatcher == referenceMatcher) {
            value = valueStr;
            type = Field.Type.REFERENCE;
        } else {
            value = valueStr;
            type = Field.Type.CONCATENATION;
        }

        field.setValue(value);
        field.setType(type);
        return field;
    }

    /**
     * Holds instance of this singleton class.
     */
    private static class ParserHolder {
        /**
         * Instance of this singleton class.
         */
        private static final SingletonParser INSTANCE = new SingletonParser();
    }
}
