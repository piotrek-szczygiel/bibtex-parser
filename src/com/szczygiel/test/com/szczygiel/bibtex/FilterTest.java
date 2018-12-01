package com.szczygiel.bibtex;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertTrue;

/**
 * Tests for {@link Entry} filtering.
 */
public class FilterTest {

    /**
     * Test filtering by type.
     */
    @Test
    public void testFilterTypes() {
        Entry entry1 = new Entry();
        entry1.setEntryType("book");

        Entry entry2 = new Entry();
        entry2.setEntryType("article");

        Entry entry3 = new Entry();
        entry3.setEntryType("mastersthesis");

        Entry entry4 = new Entry();
        entry4.setEntryType("book");

        List<Entry> entries = new ArrayList<>(List.of(entry1, entry2, entry3, entry4));
        List<Entry> filteredEntries = Filter.filterTypes(entries, Set.of("book", "article"));

        assertTrue(filteredEntries.containsAll(List.of(entry1, entry2, entry4)));
    }

    /**
     * Test filtering by author.
     */
    @Test
    public void testFilterAuthors() {
        Entry entry1 = new Entry();
        entry1.setEntryType("book");
        entry1.addAuthor("John", "Wick", Entry.AuthorType.AUTHOR);
        entry1.addAuthor("Cezary", "Pazura", Entry.AuthorType.AUTHOR);

        Entry entry2 = new Entry();
        entry2.setEntryType("article");
        entry2.addAuthor("Cezary", "Pazura", Entry.AuthorType.EDITOR);

        Entry entry3 = new Entry();
        entry3.setEntryType("mastersthesis");
        entry3.addAuthor("John", "Wick", Entry.AuthorType.AUTHOR);

        Entry entry4 = new Entry();
        entry4.setEntryType("book");
        entry4.addAuthor("Corey", "Taylor", Entry.AuthorType.AUTHOR);
        entry4.addAuthor("Cezary", "Pazura", Entry.AuthorType.EDITOR);

        List<Entry> entries = new ArrayList<>(List.of(entry1, entry2, entry3, entry4));

        List<Entry> filteredEntries = Filter.filterAuthors(entries, Set.of("Pazura"));
        assertTrue(filteredEntries.containsAll(List.of(entry1, entry2, entry4)));
    }
}
