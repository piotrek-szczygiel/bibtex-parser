package com.szczygiel.bibtex;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Tests for the BiBteX parser.
 */
public class ParserTest {
    private Parser parser;

    /**
     * Setup before tests.
     */
    @BeforeClass
    void setup() {
        parser = Parser.getInstance();
    }

    /**
     * Entry parsing test.
     */
    @Test
    public void testParseEntry() {
        String entryStr =
                "@TEST{citation_key," +
                        "key1 = 123," +
                        "key2 = \"123\"," +
                        "key3 = a # b," +
                        "author = \"author\"" +
                        "}";

        Entry entry = parser.parseEntry(entryStr);
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

    /**
     * Field parsing test.
     */
    @Test
    public void testParseField() {
        String field1Str = "key = \"string\"";
        String field2Str = "year = 1987";
        String field3Str = "month = sep";
        String field4Str = "note = \"Hello \" # \"world!\"";
        String field5Str = "month = sep # \"-\" # oct";

        Field field1 = parser.parseField(field1Str);
        Field field2 = parser.parseField(field2Str);
        Field field3 = parser.parseField(field3Str);
        Field field4 = parser.parseField(field4Str);
        Field field5 = parser.parseField(field5Str);

        assertEquals(field1.toString(), "key(string): string");
        assertEquals(field2.toString(), "year(number): 1987");
        assertEquals(field3.toString(), "month(reference): sep");
        assertEquals(field4.toString(), "note(concatenation): \"Hello \" # \"world!\"");
        assertEquals(field5.toString(), "month(concatenation): sep # \"-\" # oct");
    }

    /**
     * Entry cutting test.
     */
    @Test
    public void testCutEntry() {
        String validEntryStr = "@TEST{citation_key, key = value, key2 = \"{value}\", key3 = 23}";
        String entryStr = validEntryStr + ", key4 = 56}";

        String cut = parser.cutEntry(entryStr, 0);
        assertEquals(cut, validEntryStr);
    }
}
