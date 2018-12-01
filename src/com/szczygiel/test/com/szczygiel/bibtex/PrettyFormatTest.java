package com.szczygiel.bibtex;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Tests for the pretty formatting of BibTeX entries.
 */
public class PrettyFormatTest {

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
                "   note = \"This is a full TECHREPORT entry\"\n" +
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
                "╚═════════════╧═════════════════════════════════════════════════╝";

        Entry entry = Parser.getInstance().parseEntry(entryStr);
        entry.fillAuthors();
        assertEquals(PrettyFormat.table(entry), validTable);
    }
}
