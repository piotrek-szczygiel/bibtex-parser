package com.szczygiel.bibtex;

import java.util.*;
import java.util.regex.Matcher;

/**
 * Stores information about entry, provides strings and concatenation computing.
 */
public class Entry {
    private String entryType;
    private String citationKey;
    private Set<String> authors;

    private List<Field> fields;

    /**
     * Constructor for Entry.
     * <p>
     * Initializes members with default values.
     */
    Entry() {
        entryType = "";
        citationKey = "";
        fields = new ArrayList<>();
        authors = new HashSet<>();
    }

    /**
     * Return entry type.
     * <p>
     * Entry type can be for example: "book"
     *
     * @return entry type
     */
    String getEntryType() {
        return entryType;
    }

    /**
     * Set entry type.
     *
     * @param entryType entry type
     */
    void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    /**
     * Return citation key.
     * <p>
     * Citation key is defined like this: @TYPE{citation_key
     *
     * @return citation key
     */
    String getCitationKey() {
        return citationKey;
    }

    /**
     * Set citation key.
     *
     * @param citationKey citation key
     */
    void setCitationKey(String citationKey) {
        this.citationKey = citationKey;
    }

    /**
     * Get list iterator over Field list.
     *
     * @return Fields iterator
     */
    ListIterator<Field> fieldsIterator() {
        return fields.listIterator();
    }

    /**
     * Get list of fields.
     *
     * @return list of fields
     */
    List<Field> getFields() {
        return fields;
    }

    /**
     * Get specific field.
     *
     * @param key field's key value
     * @return Field object or null when field is not found
     */
    Field getField(String key) {
        for (Field field : fields) {
            if (field.getKey().equals(key)) {
                return field;
            }
        }

        return null;
    }

    /**
     * Add Field.
     *
     * @param field Field object to add
     */
    void addField(Field field) {
        fields.add(field);
    }

    /**
     * Add author.
     * <p>
     * Needed for filtering entries by author's name..
     *
     * @param author author's name
     */
    void addAuthor(String author) {
        authors.add(author);
    }

    /**
     * Get authors' names.
     *
     * @return Set of authors' names
     */
    Set<String> getAuthors() {
        return authors;
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

    /**
     * Convert entry into ASCII table.
     *
     * @return ASCII string
     */
    @Override
    public String toString() {
        return PrettyFormat.table(this);
    }
}
