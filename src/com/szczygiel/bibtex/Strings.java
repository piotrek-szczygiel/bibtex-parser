package com.szczygiel.bibtex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Strings {
    private Map<String, String> strings = new HashMap<>();

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
                    continue;
                }

                if (field.getType() != Field.Type.STRING) {
                    System.out.println("non string object in @string: " + field);
                    continue;
                }

                String value = (String) field.getValue();
                strings.putIfAbsent(key, value);
            }
        }
    }

    String getString(String key) {
        return strings.getOrDefault(key, "");
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, String> entry : strings.entrySet()) {
            str.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
        }

        return str.toString();
    }
}
