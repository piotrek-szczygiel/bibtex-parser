package com.szczygiel.bibtex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PatternMatcher {
    private Pattern patternEntry, patternEntryBeginning, patternField, patternString,
            patternNumber, patternReference, patternConcatenation;

    PatternMatcher() {
        patternEntry = Pattern.compile("(?s)(?m)^@([a-zA-Z_][\\w-]*)\\s*\\{(?:\\s*([a-zA-Z_][\\w-]*)\\s*,\\s*)?((.)*)" +
                "\\s*}");
        patternEntryBeginning = Pattern.compile("(?m)^@([a-zA-Z_][\\w-]*).*");
        patternField = Pattern.compile("\\s*(([^,])*),?\\s*");
        patternString = Pattern.compile("(?s)\\s*([a-zA-Z_][\\w-]*)\\s*=\\s*[\"{]((.)*)[\"}]\\s*$");
        patternNumber = Pattern.compile("\\s*([a-zA-Z_][\\w-]*)\\s*=\\s*(\\d+)\\s*$");
        patternReference = Pattern.compile("\\s*([a-zA-Z_][\\w-]*)\\s*=\\s*([a-zA-Z_][\\w-]*)\\s*$");
        patternConcatenation = Pattern.compile("(?s)\\s*([a-zA-Z_][\\w-]*)\\s*=\\s*((.)*#(.)*)\\s*$");
    }

    Matcher matchEntry(String input) {
        return patternEntry.matcher(input);
    }

    Matcher matchEntryBeginning(String input) {
        return patternEntryBeginning.matcher(input);
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

    Matcher matchConcatenation(String input) {
        return patternConcatenation.matcher(input);
    }
}
