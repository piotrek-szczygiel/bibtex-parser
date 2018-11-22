package com.szczygiel.bibtex;

/**
 * Stores information about field.
 */
class Field {
    private String key;

    private String raw;
    private Object value;
    private Type type = Type.UNKNOWN;

    String getRaw() {
        return raw;
    }

    void setRaw(String raw_) {
        raw = raw_;
    }

    String getKey() {
        return key;
    }

    void setKey(String key_) {
        key = key_;
    }

    Object getValue() {
        return value;
    }

    void setValue(Object value_) {
        value = value_;
    }

    Type getType() {
        return type;
    }

    void setType(Type type_) {
        type = type_;
    }

    @Override
    public String toString() {
        String str = "";
        switch (type) {
            case STRING:
                str = key + "(string): " + value;
                break;
            case NUMBER:
                str = key + "(number): " + value;
                break;
            case REFERENCE:
                str = key + "(reference): " + value;
                break;
            case CONCATENATION:
                str = key + "(concatenation): " + value;
                break;
            case UNKNOWN:
                str = key + "(unknown): " + raw;
                break;
        }

        return str;
    }

    /**
     * Available types of fields.
     */
    enum Type {
        STRING, NUMBER, REFERENCE, CONCATENATION, UNKNOWN
    }
}
