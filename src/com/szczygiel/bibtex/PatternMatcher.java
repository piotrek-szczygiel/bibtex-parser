package com.szczygiel.bibtex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {
    private Pattern patternEntry, patternSplit, patternString, patternNumber, patternReference;

    public PatternMatcher() {
        patternEntry = Pattern.compile("(?s)(?m)^@(\\w+)\\s*\\{\\s*([\\w-]+)\\s*,\\s*((.)*?)^}$");
        patternSplit = Pattern.compile("\\s*(([^,])*)\\s*,?\\s*");
        patternString = Pattern.compile("\\s*(\\w+)\\s*=\\s*(\"(.)*\")\\s*");
        patternNumber = Pattern.compile("\\s*(\\w+)\\s*=\\s*(\\d+)\\s*");
        patternReference = Pattern.compile("\\s*(\\w+)\\s*=\\s*(\\w+)\\s*");
    }

    protected Matcher matchEntry(String input) {
        return patternEntry.matcher(input);
    }

    protected Matcher matchSplit(String input) {
        return patternSplit.matcher(input);
    }

    protected Matcher matchString(String input) {
        return patternString.matcher(input);
    }

    protected Matcher matchNumber(String input) {
        return patternNumber.matcher(input);
    }

    protected Matcher matchReference(String input) {
        return patternReference.matcher(input);
    }
}
