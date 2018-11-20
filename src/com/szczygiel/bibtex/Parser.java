package com.szczygiel.bibtex;

import java.util.regex.Matcher;

public class Parser {
    private PatternMatcher patternMatcher;

    public Parser() {
        patternMatcher = new PatternMatcher();
    }

    public void parse(String input) {
        Matcher entryMatcher = patternMatcher.matchEntry(input);

        // Find an entry
        while (entryMatcher.find()) {
            String entryType = entryMatcher.group(1);
            String citationKey = entryMatcher.group(2);
            String keyValueStructure = entryMatcher.group(3);

            System.out.println(citationKey + "(" + entryType + ")");

            Matcher splitMatcher = patternMatcher.matchSplit(keyValueStructure);

            // Find key value combination
            while (splitMatcher.find()) {
                String keyValue = splitMatcher.group(1);
                System.out.println(keyValue);

                Matcher stringMatcher = patternMatcher.matchString(keyValue);
                Matcher numberMatcher = patternMatcher.matchNumber(keyValue);
                Matcher referenceMatcher = patternMatcher.matchReference(keyValue);

                while (stringMatcher.find()) {
                    System.out.println("found string");
                }
            }
        }
    }
}
