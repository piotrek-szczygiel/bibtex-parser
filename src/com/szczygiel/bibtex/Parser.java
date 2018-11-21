package com.szczygiel.bibtex;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

class Parser {
    private PatternMatcher patternMatcher;
    private int lastEndingIndex = 0;

    Parser() {
        patternMatcher = new PatternMatcher();
    }

    List<Entry> parse(String input) {
        List<Entry> entries = new ArrayList<>();

        Matcher entryBeginningMatcher = patternMatcher.matchEntryBeginning(input);
        while (entryBeginningMatcher.find()) {
            String entryStr = cutEntry(entryBeginningMatcher, input);
            Matcher entryMatcher = patternMatcher.matchEntry(entryStr);

            if (entryMatcher.find()) {
                Entry entry = parseEntry(entryMatcher);
                if (entry != null) {
                    entries.add(entry);
                }
            }
        }

        return entries;
    }

    private String cutEntry(Matcher entryBeginningMatcher, String input) {
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

    @Nullable
    private Entry parseEntry(Matcher entryMatcher) {
        Entry entry = new Entry();

        if (entryMatcher.groupCount() < 2) {
            return null;
        }

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

    @Nullable
    private Field parseField(Matcher fieldMatcher) {
        String keyValue = fieldMatcher.group(1);
        if (keyValue == null || keyValue.equals("")) {
            return null;
        }

        Field field = new Field();
        field.setRaw(keyValue);

        String key;
        Object value;
        Field.Type type;

        Matcher stringMatcher = patternMatcher.matchString(keyValue);
        Matcher numberMatcher = patternMatcher.matchNumber(keyValue);
        Matcher referenceMatcher = patternMatcher.matchReference(keyValue);
        Matcher concatenationMatcher = patternMatcher.matchConcatenation(keyValue);

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
