package com.szczygiel.bibtex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Parses BiBteX input.
 */
class Parser {
    /**
     * Stores ending index of last {@link #cutEntry} result.
     */
    static private int lastEndingIndex;

    /**
     * Parses BiBteX input into list of {@link Entry entries}.
     *
     * @param input BiBteX input
     * @return list of {@link Entry entries}
     */
    static List<Entry> parse(String input) {
        List<Entry> entries = new ArrayList<>();

        Matcher entryBeginningMatcher = Patterns.entryBeginning.matcher(input);
        lastEndingIndex = 0;
        while (entryBeginningMatcher.find()) {
            String entryStr = cutEntry(entryBeginningMatcher, input);
            if (entryStr == null) {
                continue;
            }

            Entry entry = parseEntry(entryStr);
            if (entry != null) {
                entries.add(entry);
            }
        }

        return entries;
    }

    /**
     * Cuts an entry until last bracket.
     * <p>
     * Allows for nested brackets.
     *
     * @param entryBeginningMatcher {@link Matcher} used for finding beginning of an entry
     * @param input                 BiBteX input
     * @return single BiBteX entry as {@link String}
     */
    static private String cutEntry(Matcher entryBeginningMatcher, String input) {
        int matchStartIndex = entryBeginningMatcher.start();
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
     * Parses BiBteX entry into an {@link Entry} object.
     * <p>
     * Ignores @PREAMBLE and @COMMENT entries.
     *
     * @param entryStr entry to parse
     * @return parsed {@link Entry}
     */
    static Entry parseEntry(String entryStr) {
        entryStr = entryStr.strip();
        Matcher entryMatcher = Patterns.entry.matcher(entryStr);

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
        Matcher fieldMatcher = Patterns.field.matcher(keyValueStructure);

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
     * Parses BiBteX field into a {@link Field} object.
     *
     * @param input BiBteX field
     * @return parsed {@link Field}
     */
    static private Field parseField(String input) {
        input = input.strip();
        if (input == null || input.equals("")) {
            return null;
        }

        Field field = new Field();
        field.setRaw(input);

        String key;
        Object value;
        Field.Type type;

        Matcher stringMatcher = Patterns.string.matcher(input);
        Matcher numberMatcher = Patterns.number.matcher(input);
        Matcher referenceMatcher = Patterns.reference.matcher(input);
        Matcher concatenationMatcher = Patterns.concatenation.matcher(input);

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
}
