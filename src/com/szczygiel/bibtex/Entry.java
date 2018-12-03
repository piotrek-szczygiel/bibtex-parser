package com.szczygiel.bibtex;

import java.util.*;
import java.util.regex.Matcher;

/**
 * Stores information about an entry, provides strings and concatenation computing.
 */
public class Entry {
    /**
     * Stores information about author's names.
     */
    class Author {
        /**
         * Author's first name.
         */
        String firstName;

        /**
         * Author's last name.
         */
        String lastName;

        /**
         * Author's type.
         * <p>
         * Can be author or editor.
         */
        AuthorType authorType;
    }

    /**
     * Author's type.
     */
    enum AuthorType {
        /**
         * This means that this author was specified like this: author = "name".
         */
        AUTHOR,

        /**
         * This means that this author was specified like this: editor = "name".
         */
        EDITOR
    }

    /**
     * Set of authors' names used later in filtering.
     */
    private Set<Author> authors;

    /**
     * Citation key of an entry.
     * <p>
     * Citation key can be defined inside BibTeX file like this: @TYPE{citation_key
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
     * Line number in file at which this entry exists.
     */
    private int lineNumber;

    /**
     * Fill {@link #authors} based on author or editor fields.
     */
    void fillAuthors() {
        for (Field field : fields) {
            String fieldKey = field.getKey();
            if (field.getType() == Field.Type.STRING &&
                    (fieldKey.equals("author") || fieldKey.equals("editor"))) {
                String value = (String) field.getValue();
                String[] authors = value.split("and");
                for (String author : authors) {
                    author = author.strip();

                    String firstName, lastName;

                    AuthorType authorType;
                    if (fieldKey.equals("author")) {
                        authorType = AuthorType.AUTHOR;
                    } else {
                        authorType = AuthorType.EDITOR;
                    }

                    int nameDelimiterIndex = author.indexOf("|");

                    if (nameDelimiterIndex == -1) {
                        // If there is no '|' symbol - last word is last name

                        int lastSpace = author.lastIndexOf(" ");
                        if (lastSpace == -1) {
                            // There is no space in the name - everything is last name

                            firstName = "";
                            lastName = author;
                        } else {
                            // There is a space in the name - last word is last name, the rest is first name

                            firstName = author.substring(0, lastSpace);
                            lastName = author.substring(lastSpace + 1);
                        }

                    } else {
                        // Else everything to the left is last name and the rest is first name

                        firstName = author.substring(nameDelimiterIndex + 1);
                        lastName = author.substring(0, nameDelimiterIndex);
                    }

                    addAuthor(firstName.strip(), lastName.strip(), authorType);
                }
            }
        }
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
     * Get {@link #authors}.
     *
     * @return authors
     */
    Set<Author> getAuthors() {
        return authors;
    }

    /**
     * Get {@link #authors authors'} last names.
     *
     * @return authors last names
     */
    Set<String> getAuthorsLastNames() {
        Set<String> lastNames = new HashSet<>();
        for (Author author : authors) {
            lastNames.add(author.lastName);
        }

        return lastNames;
    }


    /**
     * Get {@link #lineNumber}.
     *
     * @return line number
     */
    int getLineNumber() {
        return lineNumber;
    }

    /**
     * Set {@link #lineNumber}.
     *
     * @param lineNumber line number
     */
    void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

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

                Matcher concatMatcher = Patterns.getInstance().matchConcatenationField(concatenation);

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
     * Add author to {@link #authors}.
     *
     * @param firstName  first name
     * @param lastName   last name
     * @param authorType author's type (author or editor)
     */
    void addAuthor(String firstName, String lastName, AuthorType authorType) {
        Author author = new Author();
        author.firstName = firstName;
        author.lastName = lastName;
        author.authorType = authorType;
        authors.add(author);
    }

    /**
     * Convert entry into readable structure.
     *
     * @return readable entry
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(citationKey + "(" + entryType + "): ");
        for (Field field : fields) {
            // Multiline string printing
            String fieldStr = field.toString();
            fieldStr = fieldStr.replaceAll("\\r\\n|\\r|\\n", "\n\t\t> ");

            str.append("\n\t").append(fieldStr);
        }

        return str.toString();
    }
}
