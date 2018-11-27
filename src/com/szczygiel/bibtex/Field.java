package com.szczygiel.bibtex;

/**
 * Stores information about field.
 */
class Field {
    /**
     * Get raw value of field.
     * <p>
     * Raw value is field value before parsing.
     *
     * @return raw value
     */
    String getRaw() {
        return raw;
    }

    private String key;

    private String raw;
    private Object value;
    private Type type = Type.UNKNOWN;

    /**
     * Set raw value of field.
     *
     * @param raw raw value
     */
    void setRaw(String raw) {
        this.raw = raw;
    }

    /**
     * Get field's key.
     * <p>
     * Key is like variable name e.g. key = "value"
     *
     * @return key
     */
    String getKey() {
        return key;
    }

    /**
     * Set field's key.
     *
     * @param key key name to set
     */
    void setKey(String key) {
        this.key = key;
    }

    /**
     * Get field's value.
     * <p>
     * Field can have following types:
     * - String
     * - Integer
     *
     * @return field's value
     */
    Object getValue() {
        return value;
    }

    /**
     * Set field's value.
     *
     * @param value value to set
     */
    void setValue(Object value) {
        this.value = value;
    }

    /**
     * Get type of field's vaule.
     * <p>
     * Field's value can have following types:
     * - STRING
     * - NUMBER
     * - REFERENCE
     * - CONCATENATION
     * - UNKNOWN
     *
     * @return field's value type
     */
    Type getType() {
        return type;
    }

    /**
     * Set type of field's value.
     *
     * @param type field's value type
     */
    void setType(Type type) {
        this.type = type;
    }

    /**
     * Convert field into readable string for debugging purposes.
     *
     * @return readable string
     */
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
