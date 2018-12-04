package com.szczygiel.bibtex;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Tests for the pretty formatting of BibTeX entries.
 */
public class PrettyFormatTest {
    /**
     * {@link SingletonParser} instance.
     */
    private SingletonParser parser;

    /**
     * Setup before tests.
     */
    @BeforeClass
    void setup() {
        parser = SingletonParser.getInstance();
    }

    /**
     * Tests table formatting for simple entry.
     */
    @Test
    public void testTable() {
        String entryStr = "@TECHREPORT{techreport-full,\n" +
                "   author = \"Tom Trrific\",\n" +
                "   title = \"AnSorting Algorithm\",\n" +
                "   institution = \"Fanstord University\",\n" +
                "   number = 7,\n" +
                "   address = \"Computer Science Department Fanstord California\",\n" +
                "   month = oct,\n" +
                "   year = 1988,\n" +
                "   note = \"This is a full TECHREPORT entry\ncontaining two lines\"\n" +
                "}";

        String validTable = "╔═══════════════════════════════════════════════════════════════╗\n" +
                "║ TECHREPORT (techreport-full)                                  ║\n" +
                "╠═════════════╤═════════════════════════════════════════════════╣\n" +
                "║ author      │ • Tom Trrific                                   ║\n" +
                "╟─────────────┼─────────────────────────────────────────────────╢\n" +
                "║ title       │ AnSorting Algorithm                             ║\n" +
                "╟─────────────┼─────────────────────────────────────────────────╢\n" +
                "║ institution │ Fanstord University                             ║\n" +
                "╟─────────────┼─────────────────────────────────────────────────╢\n" +
                "║ number      │ 7                                               ║\n" +
                "╟─────────────┼─────────────────────────────────────────────────╢\n" +
                "║ address     │ Computer Science Department Fanstord California ║\n" +
                "╟─────────────┼─────────────────────────────────────────────────╢\n" +
                "║ month       │ oct                                             ║\n" +
                "╟─────────────┼─────────────────────────────────────────────────╢\n" +
                "║ year        │ 1988                                            ║\n" +
                "╟─────────────┼─────────────────────────────────────────────────╢\n" +
                "║ note        │ This is a full TECHREPORT entry                 ║\n" +
                "║             │ containing two lines                            ║\n" +
                "╚═════════════╧═════════════════════════════════════════════════╝";

        Entry entry = parser.parseEntry(entryStr);
        entry.fillAuthors();
        assertEquals(PrettyFormat.table(entry), validTable);
    }

    /**
     * Test narrow table formatting.
     */
    @Test
    public void testNarrowTable() {
        String entryStr = "@ENTRY_TYPE{citation_key,\n" +
                "    key1 = 123,\n" +
                "    key2 = \"321\"\n" +
                "}";

        String validTable = "╔═══════════════════════════╗\n" +
                "║ ENTRY_TYPE (citation_key) ║\n" +
                "╠══════╤════════════════════╣\n" +
                "║ key1 │ 123                ║\n" +
                "╟──────┼────────────────────╢\n" +
                "║ key2 │ 321                ║\n" +
                "╚══════╧════════════════════╝";

        Entry entry = parser.parseEntry(entryStr);
        assertEquals(PrettyFormat.table(entry), validTable);
    }

    /**
     * Test table containing both authors and editors.
     */
    @Test
    public void testTableWithEditorAndAuthor() {
        String entryStr = "@INCOLLECTION{incollection-full,\n" +
                "   author = \"Daniel D. Lincoll\",\n" +
                "   title = \"Semigroups of Recurrences\",\n" +
                "   editor = \"David J. Lipcoll and D. H. Lawrie and A. H. Sameh\",\n" +
                "   booktitle = \"High Speed Computer and Algorithm Organization\",\n" +
                "   number = 23,\n" +
                "   series = \"Fast Computers\",\n" +
                "   chapter = 3,\n" +
                "   type = \"Part\",\n" +
                "   pages = \"179 183\",\n" +
                "   publisher = \"Academic Press\",\n" +
                "   address = \"New York\",\n" +
                "   edition = \"Third\",\n" +
                "   month = sep,\n" +
                "   year = 1977,\n" +
                "   note = \"This is a full INCOLLECTION entry\"\n" +
                "}";

        String validTable = "╔═════════════════════════════════════════════════════════════════╗\n" +
                "║ INCOLLECTION (incollection-full)                                ║\n" +
                "╠═══════════╤═════════════════════════════════════════════════════╣\n" +
                "║ author    │ • Daniel D. Lincoll                                 ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ title     │ Semigroups of Recurrences                           ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ editor    │ • David J. Lipcoll                                  ║\n" +
                "║           │ • D. H. Lawrie                                      ║\n" +
                "║           │ • A. H. Sameh                                       ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ booktitle │ High Speed Computer and Algorithm Organization      ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ number    │ 23                                                  ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ series    │ Fast Computers                                      ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ chapter   │ 3                                                   ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ type      │ Part                                                ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ pages     │ 179 183                                             ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ publisher │ Academic Press                                      ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ address   │ New York                                            ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ edition   │ Third                                               ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ month     │ sep                                                 ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ year      │ 1977                                                ║\n" +
                "╟───────────┼─────────────────────────────────────────────────────╢\n" +
                "║ note      │ This is a full INCOLLECTION entry                   ║\n" +
                "╚═══════════╧═════════════════════════════════════════════════════╝";

        Entry entry = parser.parseEntry(entryStr);
        entry.fillAuthors();
        assertEquals(PrettyFormat.table(entry), validTable);
    }
}
