package com.szczygiel.bibtex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Filter {@link Entry entries} using specific requirements.
 * <p>
 * Uses static class design pattern.
 */
class Filter {
    /**
     * Filter {@link Entry entries} by their types.
     * <p>
     * This filter uses logic OR to filter.
     * This means that it will return {@link Entry entries} that are any of the given types.
     *
     * @param entries    list of {@link Entry entries} to filter
     * @param entryTypes wanted entry types as set of strings
     * @return filtered list of {@link Entry entries}
     */
    static List<Entry> filterTypes(List<Entry> entries, Set<String> entryTypes) {
        List<Entry> filteredEntries = new ArrayList<>();
        for (Entry entry : entries) {
            if (entryTypes.contains(entry.getEntryType())) {
                filteredEntries.add(entry);
            }
        }

        return filteredEntries;
    }

    /**
     * Filter {@link Entry entries} by authors' names.
     * <p>
     * This filter uses logic AND to filter.
     * This means that it will return {@link Entry entries} written by all of given authors.
     *
     * @param entries list of {@link Entry entries} to filter
     * @param authors wanted authors' names
     * @return filtered list of {@link Entry entries}
     */
    static List<Entry> filterAuthors(List<Entry> entries, Set<String> authors) {
        List<Entry> filteredEntries = new ArrayList<>();
        for (Entry entry : entries) {
            if (entry.getAuthorsLastNames().containsAll(authors)) {
                filteredEntries.add(entry);
            }
        }

        return filteredEntries;
    }
}
