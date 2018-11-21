package com.szczygiel.bibtex;

import java.util.regex.Matcher;

class Parser {
    private PatternMatcher patternMatcher;

    Parser() {
        patternMatcher = new PatternMatcher();
    }

    void parse(String input) {
        Matcher entryMatcher = patternMatcher.matchEntry(input);

        // Find an entry
        while (entryMatcher.find()) {

            // Parse it
            Entry entry = parseEntry(entryMatcher);
            if (entry == null) {
                continue;
            }

            System.out.println(entry);
        }
    }

    private Entry parseEntry(Matcher entryMatcher) {
        Entry entry = new Entry();

        String entryType = entryMatcher.group(1);
        entry.setEntryType(entryType);

        if (entryType.equals("String")) {
            // TODO: handle @String
            return null;
        }

        String citationKey = entryMatcher.group(2);
        entry.setCitationKey(citationKey);

        String keyValueStructure = entryMatcher.group(3);
        Matcher fieldMatcher = patternMatcher.matchField(keyValueStructure);

        // Find key value combination
        while (fieldMatcher.find()) {
            Field field = parseField(fieldMatcher);
            if (field != null) {
                entry.addField(field);
            }
        }

        return entry;
    }

    private Field parseField(Matcher fieldMatcher) {
        String keyValue = fieldMatcher.group(1);

        Matcher stringMatcher = patternMatcher.matchString(keyValue);
        Matcher numberMatcher = patternMatcher.matchNumber(keyValue);
        Matcher referenceMatcher = patternMatcher.matchReference(keyValue);
        Matcher actualMatcher = null;

        String key;
        String value;

        Field.Type type = Field.Type.UNKNOWN;

        if (stringMatcher.find()) {
            actualMatcher = stringMatcher;
            type = Field.Type.STRING;
        } else if (numberMatcher.find()) {
            actualMatcher = numberMatcher;
            type = Field.Type.NUMBER;
        } else if (referenceMatcher.find()) {
            actualMatcher = referenceMatcher;
            type = Field.Type.REFERENCE;
        }

        if (actualMatcher != null) {
            Field field = new Field();

            key = actualMatcher.group(1);
            field.setKey(key);

            value = actualMatcher.group(2);

            switch (type) {
                case STRING:
                    field.setString(value);
                    break;
                case NUMBER:
                    field.setNumber(value);
                    break;
                case REFERENCE:
                    field.setReference(value);
                    break;
            }

            return field;
        }

        return null;
    }
}
