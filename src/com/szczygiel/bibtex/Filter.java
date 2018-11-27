package com.szczygiel.bibtex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Filter entries using specific requirements.
 */
class Filter {
    /**
     * Filter entries by their types.
     * <p>
     * This filter uses logic OR to filter.
     * This means that it will return entries that are any of the given types.
     *
     * @param entries    list of entries to filter
     * @param entryTypes wanted entry types
     * @return filtered list of entries
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
     * Filter entries by authors' names.
     * <p>
     * This filter uses logic AND to filter.
     * This means that it will return publications written by all of given authors.
     *
     * @param entries list of entries to filter
     * @param authors wanted authors' names
     * @return filtered list of entries
     */
    static List<Entry> filterAuthors(List<Entry> entries, Set<String> authors) {
        List<Entry> filteredEntries = new ArrayList<>();
        for (Entry entry : entries) {
            if (entry.getAuthors().containsAll(authors)) {
                filteredEntries.add(entry);
            }
        }

        return filteredEntries;
    }
}
