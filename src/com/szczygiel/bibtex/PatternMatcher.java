package com.szczygiel.bibtex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PatternMatcher {
    private Pattern patternEntry, patternField, patternString, patternNumber, patternReference;

    PatternMatcher() {
        patternEntry = Pattern.compile("(?s)(?m)^@(\\w+)\\s*\\{\\s*([\\w-]+)\\s*((.)*?)}\\s*$");
        patternField = Pattern.compile("\\s*,\\s*(([^,\\r\\n])*)\\s*");
        patternString = Pattern.compile("\\s*(\\w+)\\s*=\\s*\"((.)*)\"\\s*");
        patternNumber = Pattern.compile("\\s*(\\w+)\\s*=\\s*(\\d+)\\s*");
        patternReference = Pattern.compile("\\s*(\\w+)\\s*=\\s*([a-zA-Z]+)\\s*");
    }

    Matcher matchEntry(String input) {
        return patternEntry.matcher(input);
    }

    Matcher matchField(String input) {
        return patternField.matcher(input);
    }

    Matcher matchString(String input) {
        return patternString.matcher(input);
    }

    Matcher matchNumber(String input) {
        return patternNumber.matcher(input);
    }

    Matcher matchReference(String input) {
        return patternReference.matcher(input);
    }
}
