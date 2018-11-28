package com.szczygiel.bibtex;

/**
 * Stores information about field.
 */
class Field {
    /**
     * Key is like a variable name in a field.
     * <p>
     * E.g. key = "value"
     */
    private String key;
    /**
     * Raw value of a field.
     * <p>
     * It is field's value before parsing.
     */
    private String raw;
    /**
     * {@link Type Field's type}.
     */
    private Type type = Type.UNKNOWN;
    /**
     * Field's value.
     * <p>
     * It can have following types:
     * - {@link String}
     * - {@link Integer}
     */
    private Object value;

    /**
     * Get {@link #key}.
     *
     * @return key
     */
    String getKey() {
        return key;
    }

    /**
     * Set {@link #key}.
     *
     * @param key key
     */
    void setKey(String key) {
        this.key = key;
    }

    /**
     * Get {@link #raw raw value}.
     *
     * @return raw value
     */
    String getRaw() {
        return raw;
    }

    /**
     * Set {@link #raw raw value}.
     *
     * @param raw raw value
     */
    void setRaw(String raw) {
        this.raw = raw;
    }

    /**
     * Get {@link #type}.
     *
     * @return type
     */
    Type getType() {
        return type;
    }

    /**
     * Set {@link #type}.
     *
     * @param type type
     */
    void setType(Type type) {
        this.type = type;
    }

    /**
     * Get {@link #value}.
     *
     * @return value
     */
    Object getValue() {
        return value;
    }

    /**
     * Set {@link #value}.
     *
     * @param value value
     */
    void setValue(Object value) {
        this.value = value;
    }

    /**
     * Available types of fields.
     */
    enum Type {
        /**
         * Simple string value.
         */
        STRING,

        /**
         * Integer value.
         */
        NUMBER,

        /**
         * Reference to string stored in {@link Strings}.
         */
        REFERENCE,

        /**
         * Concatenation of combination of strings and references.
         */
        CONCATENATION,

        /**
         * Unknown, invalid field type.
         */
        UNKNOWN
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
}
