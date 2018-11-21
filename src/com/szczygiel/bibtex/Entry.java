package com.szczygiel.bibtex;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Entry {
    private String entryType;
    private String citationKey;

    private List<Field> fields;

    Entry() {
        entryType = "";
        citationKey = "";
        fields = new ArrayList<>();
    }

    String getEntryType() {
        return entryType;
    }

    void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getCitationKey() {
        return citationKey;
    }

    void setCitationKey(String citationKey) {
        this.citationKey = citationKey;
    }

    List<Field> getFields() {
        return fields;
    }

    @Nullable
    Field getField(String key) {
        for (Field field : fields) {
            if (field.getKey().equals(key)) {
                return field;
            }
        }

        return null;
    }

    void addField(Field field) {
        fields.add(field);
    }

    void computeStrings(Strings strings) {
        for (Field field : fields) {
            if (field.getType() == Field.Type.REFERENCE) {
                String reference = (String) field.getValue();
                String value = strings.getString(reference);

                field.setType(Field.Type.STRING);
                field.setValue(value);
            }
        }

        computeConcatenation(strings);
    }

    static private Pattern patternConcat = Pattern.compile("(?s)\\s*([\"{].*[\"}]|[a-zA-Z_][\\w-]*)\\s*(#)?\\s*");

    private void computeConcatenation(Strings strings) {
        for (Field field : fields) {
            if (field.getType() == Field.Type.CONCATENATION) {
                StringBuilder finalValue = new StringBuilder();
                String concatenation = (String) field.getValue();

                Matcher concatMatcher = patternConcat.matcher(concatenation);
                while (concatMatcher.find()) {
                    if (concatMatcher.groupCount() < 2) {
                        break;
                    }
                    String value = concatMatcher.group(1);
                    String hash = concatMatcher.group(2);

                    if (value.startsWith("\"")) {
                        if (!value.endsWith("\"")) {
                            break;
                        }

                        value = value.substring(1, value.length() - 1);
                    } else if (value.startsWith("{")) {
                        if (!value.endsWith("}")) {
                            break;
                        }

                        value = value.substring(1, value.length() - 1);
                    } else {
                        value = strings.getString(value);
                    }

                    finalValue.append(value);

                    if (hash == null) {
                        break;
                    }
                }

                field.setType(Field.Type.STRING);
                field.setValue(finalValue.toString());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(citationKey + "(" + entryType + ") =");
        for (Field field : fields) {
            str.append("\n\t").append(field);
        }

        return str.toString();
    }
}
