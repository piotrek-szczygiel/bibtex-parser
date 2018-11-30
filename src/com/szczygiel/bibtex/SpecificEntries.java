package com.szczygiel.bibtex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds list of entries with their required and optional fields.
 * <p>
 * Uses singleton design pattern.
 *
 * @see <a href="https://pl.wikipedia.org/wiki/BibTeX#Struktura_plik.C3.B3w_bazy_bibliograficznej">Wikipedia article</a>
 */
class SpecificEntries {
    /**
     * Stores map of entry types and its required and optional fields.
     */
    private Map<String, SpecificEntry> specificEntries = new HashMap<>();

    /**
     * Populates {@link #specificEntries}.
     */
    private SpecificEntries() {
        specificEntries.put("article",
                new SpecificEntry(
                        List.of("author", "title", "journal", "year"),
                        List.of("volume", "number", "pages", "month", "note", "key")));
        specificEntries.put("book",
                new SpecificEntry(
                        List.of("author|editor", "title", "publisher", "year"),
                        List.of("volume", "series", "address", "edition", "month", "note", "key")));
        specificEntries.put("inproceedings",
                new SpecificEntry(
                        List.of("author", "title", "booktitle", "year"),
                        List.of("editor", "volume|number", "series", "pages", "address", "month", "organization",
                                "publisher", "note", "key")));
        specificEntries.put("conference",
                new SpecificEntry(
                        List.of("author", "title", "booktitle", "year"),
                        List.of("editor", "volume|number", "series", "pages", "address", "month", "organization",
                                "publisher", "note", "key")));
        specificEntries.put("booklet",
                new SpecificEntry(
                        List.of("title"),
                        List.of("author", "howpublished", "address", "month", "year", "note", "key")));
        specificEntries.put("inbook",
                new SpecificEntry(
                        List.of("author|editor", "title", "chapter|pages", "publisher", "year"),
                        List.of("volume|number", "series", "type", "address", "edition", "month", "note", "key")));
        specificEntries.put("incollection",
                new SpecificEntry(
                        List.of("author", "title", "booktitle", "publisher", "year"),
                        List.of("editor", "volume|number", "series", "type", "chapter", "pages", "address", "edition",
                                "month", "note", "key")));
        specificEntries.put("manual",
                new SpecificEntry(
                        List.of("title"),
                        List.of("author", "organization", "address", "edition", "month", "year", "note", "key")));
        specificEntries.put("mastersthesis",
                new SpecificEntry(
                        List.of("author", "title", "school", "year"),
                        List.of("type", "address", "month", "note", "key")));
        specificEntries.put("phdthesis",
                new SpecificEntry(
                        List.of("author", "title", "school", "year"),
                        List.of("type", "address", "month", "note", "key")));
        specificEntries.put("techreport",
                new SpecificEntry(
                        List.of("author", "title", "institution", "year"),
                        List.of("editor", "volume|number", "series", "address", "month", "organization", "publisher",
                                "note", "key")));
        specificEntries.put("misc",
                new SpecificEntry(
                        List.of(""),
                        List.of("author", "title", "howpublished", "month", "year", "note", "key")));
        specificEntries.put("unpublished",
                new SpecificEntry(
                        List.of("author", "title", "note"),
                        List.of("month", "year", "key")));
    }

    /**
     * Return instance of this singleton class.
     *
     * @return instanc eof {@link SpecificEntries}
     */
    static SpecificEntries getInstance() {
        return SpecificEntriesHolder.INSTANCE;
    }

    /**
     * Get {@link SpecificEntry} by its type.
     *
     * @param entryType entry type
     * @return {@link SpecificEntry} or null when it is not found
     */
    SpecificEntry get(String entryType) {
        return specificEntries.get(entryType);
    }

    /**
     * Helper class for populating specific entries.
     */
    static class SpecificEntry {
        /**
         * Fields required by specific entry type.
         */
        List<String> requiredFields;

        /**
         * Optional fields in specific entry type.
         * <p>
         * Fields that are neither required nor optional are omitted.
         */
        List<String> optionalFields;

        /**
         * Constructor for {@link SpecificEntry}
         *
         * @param requiredFields {@link #requiredFields}
         * @param optionalFields {@link #optionalFields}
         */
        SpecificEntry(List<String> requiredFields, List<String> optionalFields) {
            this.requiredFields = requiredFields;
            this.optionalFields = optionalFields;
        }
    }

    /**
     * Holds instance of this singleton class.
     */
    private static class SpecificEntriesHolder {
        /**
         * Instance of this singleton class.
         */
        private static final SpecificEntries INSTANCE = new SpecificEntries();
    }
}
