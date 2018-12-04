package com.szczygiel.bibtex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains regex matchers used in this project.
 * <p>
 * Uses singleton design pattern.
 */
class SingletonPatterns {
    /**
     * Matches beginning of an entry.
     */
    private Pattern entryBeginning = Pattern.compile("(?m)"
            + "^@([a-zA-Z_][\\w-]*)" // beginning of an entry
    );

    /**
     * Matches whole entry, catching entry type, citation key and its body.
     */
    private Pattern entry = Pattern.compile("(?s)(?m)"
            + "^@([a-zA-Z_][\\w-]*)\\s*"                // entry type
            + "\\{(?:\\s*([a-zA-Z_][\\w-]*)\\s*,\\s*)?" // citation key
            + "((.)*)\\s*}"                             // entry contents
    );

    /**
     * Matches a field separated by comma.
     */
    private Pattern field = Pattern.compile("(?m)"
            + "([^,]*),?" // comma separated fields
    );

    /**
     * Matches assignment of a string to a key.
     */
    private Pattern string = Pattern.compile("(?s)"
            + "^([a-zA-Z_][\\w-]*)\\s*" // string name
            + "=\\s*"
            + "\"(([^\"])*)\"\\s*$"     // string value
    );

    /**
     * Matches assignment of a number to a key.
     */
    private Pattern number = Pattern.compile(""
            + "^([a-zA-Z_][\\w-]*)\\s*" // number name
            + "=\\s*"
            + "(\\d+)\\s*$"             // number value
    );

    /**
     * Matches assignment of a reference to a key.
     */
    private Pattern reference = Pattern.compile(""
            + "^([a-zA-Z_][\\w-]*)\\s*" // reference name
            + "=\\s*"
            + "([a-zA-Z_][\\w-]*)\\s*$" // reference value
    );

    /**
     * Matches concatenation.
     * <p>
     * Concatenation can be any number of references and strings separated by '#' character.
     */
    private Pattern concatenation = Pattern.compile("(?s)"
            + "^([a-zA-Z_][\\w-]*)\\s*" // concatenation name
            + "=\\s*((.)*#(.)*)\\s*$"   // concatenation contents
    );

    /**
     * Matches single concatenation field.
     * <p>
     * It can be string or reference followed by '#' character.
     */
    private Pattern concatenationField = Pattern.compile("(?s)"
            + "(\"[^\"]*\"|[a-zA-Z_][\\w-]*)\\s*" // string or reference
            + "(#)?(.*)"                          // hash character
    );

    /**
     * Prevents creating new instance of this class.
     */
    private SingletonPatterns() {

    }

    /**
     * Return instance of this singleton class.
     *
     * @return instance of {@link SingletonPatterns}
     */
    static SingletonPatterns getInstance() {
        return PatternsHolder.INSTANCE;
    }

    /**
     * Match {@link #entryBeginning}.
     *
     * @param input input
     * @return matcher
     */
    Matcher matchEntryBeginning(CharSequence input) {
        return entryBeginning.matcher(input);
    }

    /**
     * Match {@link #entry}.
     *
     * @param input input
     * @return matcher
     */
    Matcher matchEntry(CharSequence input) {
        return entry.matcher(input);
    }

    /**
     * Match {@link #field}.
     *
     * @param input input
     * @return matcher
     */
    Matcher matchField(CharSequence input) {
        return field.matcher(input);
    }

    /**
     * Match {@link #string}.
     *
     * @param input input
     * @return matcher
     */
    Matcher matchString(CharSequence input) {
        return string.matcher(input);
    }

    /**
     * Match {@link #number}.
     *
     * @param input input
     * @return matcher
     */
    Matcher matchNumber(CharSequence input) {
        return number.matcher(input);
    }

    /**
     * Match {@link #reference}.
     *
     * @param input input
     * @return matcher
     */
    Matcher matchReference(CharSequence input) {
        return reference.matcher(input);
    }

    /**
     * Match {@link #concatenation}.
     *
     * @param input input
     * @return matcher
     */
    Matcher matchConcatenation(CharSequence input) {
        return concatenation.matcher(input);
    }

    /**
     * Match {@link #concatenationField}.
     *
     * @param input input
     * @return matcher
     */
    Matcher matchConcatenationField(CharSequence input) {
        return concatenationField.matcher(input);
    }

    /**
     * Holds instance of this singleton class.
     */
    private static class PatternsHolder {
        /**
         * Instance of this singleton class.
         */
        private static final SingletonPatterns INSTANCE = new SingletonPatterns();
    }
}
