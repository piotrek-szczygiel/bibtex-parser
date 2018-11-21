package com.szczygiel.bibtex;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    private void computeConcatenation(Strings strings) {
        for (Field field : fields) {
            if (field.getType() == Field.Type.CONCATENATION) {
                String concatenation = (String) field.getValue();
                // TODO: add concatenation
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
