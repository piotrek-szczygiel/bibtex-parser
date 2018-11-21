package com.szczygiel.bibtex;

public class Field {
    private Type type;
    private String key;
    private String stringValue;
    private int numberValue;
    private String referenceValue;

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
        String str = key + "(";
        switch (type) {
            case STRING:
                str += "string) = " + stringValue;
                break;
            case NUMBER:
                str += "number) = " + numberValue;
                break;
            case REFERENCE:
                str += "reference) = " + referenceValue;
                break;
        }

        return str;
    }

    public enum Type {
        STRING, NUMBER, REFERENCE, UNKNOWN
    }
}
