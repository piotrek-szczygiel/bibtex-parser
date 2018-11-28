package com.szczygiel.bibtex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple map extension for storing string values.
 * <p>
 * Provides extracting string values from {@link Entry entries}.
 */
class Strings {
    /**
     * Stores strings and their names(keys).
     */
    private Map<String, String> strings = new HashMap<>();

    /**
     * Extracts strings from list of provided {@link Entry entries}.
     *
     * @param entries list of {@link Entry entries}
     */
    void extractFrom(List<Entry> entries) {
        for (Entry entry : entries) {
            if (!entry.getEntryType().equals("string")) {
                continue;
            }

            List<Field> fields = entry.getFields();
            for (Field field : fields) {
                String key = field.getKey();
                if (strings.containsKey(key)) {
                    System.out.println("redefinition in @strings: " + field);
                }

                if (field.getType() != Field.Type.STRING) {
                    System.out.println("non string object in @string: " + field);
                    continue;
                }

                String value = (String) field.getValue();
                strings.put(key, value);
            }
        }
    }

    /**
     * Get {@link String} from {@link #strings}.
     *
     * @param key key(name) of the string
     * @return {@link String} or null if string was not found
     */
    String getString(String key) {
        return strings.get(key);
    }

    /**
     * Add given {@link String} pair to {@link #strings}.
     *
     * @param key   key(name) of the string
     * @param value value of the string
     */
    void setString(String key, String value) {
        strings.put(key, value);
    }

    /**
     * Converts strings database to readable list for debugging purposes.
     *
     * @return multiline readable string
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, String> entry : strings.entrySet()) {
            str.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
        }

        return str.toString();
    }
}
