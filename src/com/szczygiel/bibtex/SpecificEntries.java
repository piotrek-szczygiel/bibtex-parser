package com.szczygiel.bibtex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds list of entries with their required and optional fields.
 */
class SpecificEntries {
    static private Map<String, SpecificEntry> specificEntries = new HashMap<>();

    /**
     * Populates specificEntries.
     * <p>
     * According to: https://pl.wikipedia.org/wiki/BibTeX#Struktura_plik.C3.B3w_bazy_bibliograficznej
     */
    static void populate() {
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
     * Gets specific entry by its type
     *
     * @param entryType type of an entry
     * @return specified entry
     */
    static SpecificEntry get(String entryType) {
        return specificEntries.get(entryType);
    }

    /**
     * Helper class for populating specific entries.
     */
    static class SpecificEntry {
        List<String> requiredFields;
        List<String> optionalFields;

        SpecificEntry(List<String> requiredFields, List<String> optionalFields) {
            this.requiredFields = requiredFields;
            this.optionalFields = optionalFields;
        }
    }
}
