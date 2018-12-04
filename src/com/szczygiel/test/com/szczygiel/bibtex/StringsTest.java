package com.szczygiel.bibtex;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Tests for {@link Strings}.
 */
public class StringsTest {
    /**
     * {@link SingletonParser} instance.
     */
    private SingletonParser parser;

    /**
     * Setup before tests.
     */
    @BeforeClass
    public void setUp() {
        parser = SingletonParser.getInstance();
    }

    /**
     * Test simple strings functionality.
     */
    @Test
    public void testStrings() {
        String entryStr = "@STRING{\n" +
                "    str1 = \"Hello\",\n" +
                "    str2 = \"world\",\n" +
                "    str1 = \"redefinition\"\n" +
                "}";
        Entry entry = parser.parseEntry(entryStr);

        Strings strings = new Strings();
        strings.extractFrom(List.of(entry));

        assertEquals(strings.getString("str1"), "redefinition");
        assertEquals(strings.getString("str2"), "world");

        assertEquals(strings.toString(), "str1 = redefinition\nstr2 = world\n");
    }
}
