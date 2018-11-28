package com.szczygiel.bibtex;

import java.util.*;
import java.util.regex.Matcher;

/**
 * Stores information about an entry, provides strings and concatenation computing.
 */
public class Entry {
    /**
     * Set of authors' names used later in filtering.
     */
    private Set<String> authors;

    /**
     * Citation key of an entry.
     * <p>
     * Citation key can be defined inside BiBteX file like this: @TYPE{citation_key
     */
    private String citationKey;

    /**
     * Type of an entry.
     * <p>
     * Type can be for example 'book' or 'article'.
     */
    private String entryType;

    /**
     * List of {@link Field} objects inside current entry.
     */
    private List<Field> fields;

    /**
     * Constructor for {@link Entry}.
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
     * Get {@link #entryType}.
     *
     * @return entry type
     */
    String getEntryType() {
        return entryType;
    }

    /**
     * Set {@link #entryType}.
     *
     * @param entryType entry type
     */
    void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    /**
     * Get {@link #citationKey}.
     *
     * @return citation key
     */
    String getCitationKey() {
        return citationKey;
    }

    /**
     * Set {@link #citationKey}.
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
     * Get specified {@link Field}.
     *
     * @param key {@link #citationKey}
     * @return {@link Field} object or null when {@link Field} is not found
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
     * Add {@link Field}.
     *
     * @param field {@link Field}
     */
    void addField(Field field) {
        fields.add(field);
    }

    /**
     * Add author to {@link #authors}.
     *
     * @param author author
     */
    void addAuthor(String author) {
        authors.add(author);
    }

    /**
     * Get {@link #authors}.
     *
     * @return authors
     */
    Set<String> getAuthors() {
        return authors;
    }

    /**
     * Convert reference fields into string which they reference.
     *
     * @param strings {@link Strings} containing strings which {@link Field fields} can reference to
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
     * Convert concatenation {@link Field fields} into concatenated strings.
     * <p>
     * Allows for concatenating multiple strings and references.
     *
     * @param strings {@link Strings}
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
     * Convert entry into an ASCII table.
     *
     * @return ASCII table
     */
    @Override
    public String toString() {
        return PrettyFormat.table(this);
    }
}
