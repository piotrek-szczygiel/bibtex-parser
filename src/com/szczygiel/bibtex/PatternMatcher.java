package com.szczygiel.bibtex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PatternMatcher {
    private String regexEntry = "(?s)(?m)^@([a-zA-Z_][\\w-]*)\\s*\\{(?:\\s*([a-zA-Z_][\\w-]*)\\s*,\\s*)?((.)*)\\s*}";
    private String regexEntryBeginning = "(?m)^@([a-zA-Z_][\\w-]*).*";
    private String regexField = "\\s*(([^,])*),?\\s*";
    private String regexString = "(?s)\\s*([a-zA-Z_][\\w-]*)\\s*=\\s*[\"{]((.)*)[\"}]\\s*$";
    private String regexNumber = "\\s*([a-zA-Z_][\\w-]*)\\s*=\\s*(\\d+)\\s*$";
    private String regexReference = "\\s*([a-zA-Z_][\\w-]*)\\s*=\\s*([a-zA-Z_][\\w-]*)\\s*$";
    private String regexConcatenation = "(?s)\\s*([a-zA-Z_][\\w-]*)\\s*=\\s*((.)*#(.)*)\\s*$";

    private Pattern patternEntry = Pattern.compile(regexEntry);
    private Pattern patternEntryBeginning = Pattern.compile(regexEntryBeginning);
    private Pattern patternField = Pattern.compile(regexField);
    private Pattern patternString = Pattern.compile(regexString);
    private Pattern patternNumber = Pattern.compile(regexNumber);
    private Pattern patternReference = Pattern.compile(regexReference);
    private Pattern patternConcatenation = Pattern.compile(regexConcatenation);

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
