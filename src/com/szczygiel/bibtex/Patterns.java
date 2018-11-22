package com.szczygiel.bibtex;

import java.util.regex.Pattern;

/**
 * Contains regex patterns used in this project.
 */
class Patterns {
    /**
     * Matches beginning of an entry.
     */
    static Pattern entryBeginning = Pattern.compile("(?m)"
            + "^@([a-zA-Z_][\\w-]*)" // beginning of an entry
    );

    /**
     * Matches whole entry, catching entry type, citation key and its body.
     */
    static Pattern entry = Pattern.compile("(?s)(?m)"
            + "^@([a-zA-Z_][\\w-]*)\\s*"                // entry type
            + "\\{(?:\\s*([a-zA-Z_][\\w-]*)\\s*,\\s*)?" // citation key
            + "((.)*)\\s*}"                             // entry contents
    );

    /**
     * Matches a field separated by comma.
     */
    static Pattern field = Pattern.compile("(?m)"
            + "([^,]*),?" // comma separated fields
    );

    /**
     * Matches assignment of string to a key.
     */
    static Pattern string = Pattern.compile("(?s)"
            + "^([a-zA-Z_][\\w-]*)\\s*" // string name
            + "=\\s*"
            + "\"(([^\"])*)\"\\s*$"     // string value
    );

    /**
     * Matches assignment of number to a key.
     */
    static Pattern number = Pattern.compile(""
            + "^([a-zA-Z_][\\w-]*)\\s*" // number name
            + "=\\s*"
            + "(\\d+)\\s*$"             // number value
    );

    /**
     * Matches assignment of reference to a key.
     */
    static Pattern reference = Pattern.compile(""
            + "^([a-zA-Z_][\\w-]*)\\s*" // reference name
            + "=\\s*"
            + "([a-zA-Z_][\\w-]*)\\s*$" // reference value
    );

    /**
     * Matches concatenation.
     * <p>
     * Concatenation can be any number of references and strings separated by '#' character.
     */
    static Pattern concatenation = Pattern.compile("(?s)"
            + "^([a-zA-Z_][\\w-]*)\\s*" // concatenation name
            + "=\\s*((.)*#(.)*)\\s*$"   // concatenation contents
    );

    /**
     * Matches single concatenation field.
     * <p>
     * It can be string or reference followed by '#' character.
     */
    static Pattern concatenationField = Pattern.compile("(?s)"
            + "(\"[^\"]*\"|[a-zA-Z_][\\w-]*)\\s*" // string or reference
            + "(#)?(.*)"                              // hash character
    );
}
