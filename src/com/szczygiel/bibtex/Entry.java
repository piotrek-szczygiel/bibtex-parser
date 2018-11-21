package com.szczygiel.bibtex;

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

    public String getEntryType() {
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

    boolean addField(Field field) {
        return fields.add(field);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(citationKey + "(" + entryType + ") =\n");
        for (Field field : fields) {
            str.append("\t").append(field).append("\n");
        }

        return str.toString();
    }
}
