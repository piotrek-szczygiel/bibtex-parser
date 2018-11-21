package com.szczygiel.bibtex;

public class Field {
    private Type type;
    private String key;
    private String stringValue;
    private int numberValue;
    private String referenceValue;
    private String raw;

    Field() {
        type = Type.UNKNOWN;
    }

    boolean isString() {
        return type == Type.STRING;
    }

    boolean isNumber() {
        return type == Type.NUMBER;
    }

    boolean isReference() {
        return type == Type.REFERENCE;
    }

    String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

    String getRaw() {
        return raw;
    }

    void setRaw(String raw) {
        this.raw = raw;
    }


    String getString() {
        return stringValue;
    }

    void setString(String string) {
        type = Type.STRING;
        stringValue = string;
    }

    int getNumber() {
        return numberValue;
    }

    void setNumber(String number) {
        type = Type.NUMBER;
        numberValue = Integer.parseInt(number);
    }

    String getReference() {
        return referenceValue;
    }

    void setReference(String reference) {
        type = Type.REFERENCE;
        referenceValue = reference;
    }

    @Override
    public String toString() {
        String str = "";
        switch (type) {
            case STRING:
                str += key + "(string) = " + stringValue;
                break;
            case NUMBER:
                str += key + "(number) = " + numberValue;
                break;
            case REFERENCE:
                str += key + "(reference) = " + referenceValue;
                break;
            case UNKNOWN:
                str += "raw = '" + raw + "'";
                break;
        }

        return str;
    }

    public enum Type {
        STRING, NUMBER, REFERENCE, UNKNOWN
    }
}
