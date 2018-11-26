package com.szczygiel.bibtex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Stores information about entry, provides strings and concatenation computing.
 */
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

    String getCitationKey() {
        return citationKey;
    }

    void setCitationKey(String citationKey) {
        this.citationKey = citationKey;
    }

    List<Field> getFields() {
        return fields;
    }

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

    /**
     * Converts reference fields into string which they reference
     *
     * @param strings Strings object containing strings which fields can reference to
     */
    void computeStrings(Strings strings) {
        for (Field field : fields) {
            if (field.getType() == Field.Type.REFERENCE) {
                String reference = (String) field.getValue();
                String value = strings.getString(reference);
                if (value == null) {
                    System.out.println("unable to access string reference in " + this.citationKey + ": " + reference);
                    field.setType(Field.Type.UNKNOWN);
                    continue;
                }

                field.setType(Field.Type.STRING);
                field.setValue(value);
            }
        }
    }

    /**
     * Converts concatenation fields into concatenated strings
     * <p>
     * Allows for concatenating multiple strings and references.
     *
     * @param strings Strings object containing strings which fields can reference to
     */
    void computeConcatenation(Strings strings) {
        for (Field field : fields) {
            if (field.getType() == Field.Type.CONCATENATION) {
                StringBuilder finalValue = new StringBuilder();
                String concatenation = (String) field.getValue();

                Matcher concatMatcher = Patterns.concatenationField.matcher(concatenation);

                boolean error = false;
                while (concatMatcher.find()) {
                    if (concatMatcher.groupCount() < 2) {
                        break;
                    }

                    String value = concatMatcher.group(1);
                    String hash = concatMatcher.group(2);
                    String rest = concatMatcher.group(3);

                    concatMatcher.reset(rest);


                    if (value.startsWith("\"")) {
                        if (!value.endsWith("\"")) {
                            break;
                        }

                        value = value.substring(1, value.length() - 1);
                    } else {
                        String valueNullable = strings.getString(value);
                        if (valueNullable == null) {
                            System.out.println("unable to access string reference in "
                                    + this.citationKey + ": " + value);
                            error = true;
                            break;
                        }
                        value = valueNullable;
                    }

                    finalValue.append(value);

                    if (hash == null) {
                        if (!rest.equals("")) {
                            System.out.println("error while concatenating: " + concatenation);
                            error = true;
                        }

                        break;
                    }
                }

                if (error) {
                    field.setType(Field.Type.UNKNOWN);
                    continue;
                }

                field.setType(Field.Type.STRING);
                field.setValue(finalValue.toString());
            }
        }
    }

    String toSimpleString() {
        StringBuilder str = new StringBuilder(citationKey + "(" + entryType + "): ");
        for (Field field : fields) {
            // Multiline string printing
            String fieldStr = field.toString();
            fieldStr = fieldStr.replaceAll("\\r\\n|\\r|\\n", "\n\t\t> ");

            str.append("\n\t").append(fieldStr);
        }

        return str.toString();
    }

    @Override
    public String toString() {
        return PrettyFormat.table(this);
    }
}
