package com.szczygiel.bibtex;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ParserTest {

    @Test
    public void testEntryParsing1() {
        String entryStr =
            "@TEST{citation_key," +
                "key1 = 123," +
                "key2 = \"123\"," +
                "key3 = a # b," +
                "author = \"author\"" +
            "}";

        System.out.println(entryStr);

        Entry entry = Parser.parseEntry(entryStr);
        assertNotNull(entry);

        // Test simple values
        assertEquals(entry.getEntryType(), "test");
        assertEquals(entry.getCitationKey(), "citation_key");
        assertEquals(entry.getField("key1").getValue(), 123);
        assertEquals(entry.getField("key2").getValue(), "123");
        assertEquals(entry.getFields().size(), 4);

        Strings strings = new Strings();
        strings.setString("a", "a_value ");
        strings.setString("b", "b_value");

        entry.computeStrings(strings);
        entry.computeConcatenation(strings);
        entry.fillAuthors();

        // Test references + concatenations
        assertEquals(entry.getField("key3").getValue(), "a_value b_value");

        // Test author parsing
        assertTrue(entry.getAuthorsLastNames().contains("author"));
    }
}
