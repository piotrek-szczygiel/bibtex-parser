package com.szczygiel.bibtex;

import org.testng.annotations.Test;

import java.util.Set;

import static org.testng.Assert.assertEquals;

/**
 * Integration test for BibTeX Document.
 */
public class IntegrationTest {
    /**
     * Whole document test.
     */
    @Test
    public void testWholeProgram() {
        String fileStr =
                "% Copyright (C) 1988, 2010 Oren Patashnik.\n" +
                        "% Unlimited copying and redistribution of this file are permitted if it\n" +
                        "% is unmodified.  Modifications (and their redistribution) are also\n" +
                        "% permitted, as long as the resulting file is renamed.\n" +
                        "\n" +
                        "@preamble{ \"\\newcommand{\\noopsort}[1]{} \"\n" +
                        "        # \"\\newcommand{\\printfirst}[2]{#1} \"\n" +
                        "        # \"\\newcommand{\\singleletter}[1]{#1} \"\n" +
                        "        # \"\\newcommand{\\switchargs}[2]{#2#1} \" }\n" +
                        "\n" +
                        "@STRING{\n" +
                        "   jan = \"styczeń\",\n" +
                        "   feb = \"luty\",\n" +
                        "   mar = \"marzec\",\n" +
                        "   apr = \"kwiecień\",\n" +
                        "   may = \"maj\",\n" +
                        "   jun = \"czerwiec\",\n" +
                        "   jul = \"lipiec\",\n" +
                        "   aug = \"sierpień\",\n" +
                        "   sep = \"wrzesień\",\n" +
                        "   oct = \"październik\",\n" +
                        "   nov = \"listopad\",\n" +
                        "   dev = \"grudzień\"\n" +
                        "}\n" +
                        "\n" +
                        "@STRING{\n" +
                        "   ref = \"123\"\n" +
                        "}\n" +
                        "\n" +
                        "@BOOK{book-full,\n" +
                        "   author = \"Knuth| Donald E. \",\n" +
                        "   title = \"Seminumerical Algorithms\",\n" +
                        "   volume = 2,\n" +
                        "   series = \"The Art of Computer Programming\",\n" +
                        "   publisher = \"Addison-Wesley\",\n" +
                        "   address = \"Reading Massachusetts\",\n" +
                        "   edition = \"Second\",\n" +
                        "   month = \"10 \" # jan,\n" +
                        "   year = 1981,\n" +
                        "   note = \"This is a full BOOK entry\"\n" +
                        "}\n" +
                        "\n" +
                        "@BOOKLET{booklet-full,\n" +
                        "   author = \"Jill C. Knuth\",\n" +
                        "   title = \"The Programming of Computer Art\",\n" +
                        "   howpublished = \"Vernier Art Center\",\n" +
                        "   address = \"Stanford California\",\n" +
                        "   month = feb,\n" +
                        "   year = 1988,\n" +
                        "   note = \"This is a full \" # \"BOOKLET entry\"\n" +
                        "}\n" +
                        "\n" +
                        "@INPROCEEDINGS{inproceedings-full,\n" +
                        "   author = \"Alfred V. Oaho and Jeffrey D. Ullman and Yannakakis| Mihalis \",\n" +
                        "   title = \"On Notions of Information Transfer in {VLSI} Circuits\",\n" +
                        "   editor = \"Wizard V. Oz and Mihalis Yannakakis\",\n" +
                        "   booktitle = \"Proc. Fifteenth Annual ACM STOC\",\n" +
                        "   number = 17,\n" +
                        "   series = \"All ACM Conferences\",\n" +
                        "   pages = ref # \" 133 139\",\n" +
                        "   month = mar,\n" +
                        "   year = 1983,\n" +
                        "   address = \"Boston\",\n" +
                        "   organization = \"ACM\",\n" +
                        "   publisher = \"Academic Press\",\n" +
                        "   note = \"This is a full INPROCEDINGS entry\"\n" +
                        "}";

        Document document = new Document();
        document.loadString(fileStr);
        document.parse();

        String correctBookFull = "book-full(book): \n" +
                "\tauthor(string): Knuth| Donald E. \n" +
                "\ttitle(string): Seminumerical Algorithms\n" +
                "\tpublisher(string): Addison-Wesley\n" +
                "\tyear(number): 1981\n" +
                "\tvolume(number): 2\n" +
                "\tseries(string): The Art of Computer Programming\n" +
                "\taddress(string): Reading Massachusetts\n" +
                "\tedition(string): Second\n" +
                "\tmonth(string): 10 styczeń\n" +
                "\tnote(string): This is a full BOOK entry";

        String correctBookletFull = "booklet-full(booklet): \n" +
                "\ttitle(string): The Programming of Computer Art\n" +
                "\tauthor(string): Jill C. Knuth\n" +
                "\thowpublished(string): Vernier Art Center\n" +
                "\taddress(string): Stanford California\n" +
                "\tmonth(string): luty\n" +
                "\tyear(number): 1988\n" +
                "\tnote(string): This is a full BOOKLET entry";

        String correctInproceedingsFull = "inproceedings-full(inproceedings): \n" +
                "\tauthor(string): Alfred V. Oaho and Jeffrey D. Ullman and Yannakakis| Mihalis \n" +
                "\ttitle(string): On Notions of Information Transfer in {VLSI} Circuits\n" +
                "\tbooktitle(string): Proc. Fifteenth Annual ACM STOC\n" +
                "\tyear(number): 1983\n" +
                "\teditor(string): Wizard V. Oz and Mihalis Yannakakis\n" +
                "\tseries(string): All ACM Conferences\n" +
                "\tpages(string): 123 133 139\n" +
                "\taddress(string): Boston\n" +
                "\tmonth(string): marzec\n" +
                "\torganization(string): ACM\n" +
                "\tpublisher(string): Academic Press\n" +
                "\tnote(string): This is a full INPROCEDINGS entry";

        Entry bookFull = document.getEntries().get(0);
        Entry bookletFull = document.getEntries().get(1);
        Entry inproceedingsFull = document.getEntries().get(2);

        assertEquals(bookFull.toString(), correctBookFull);
        assertEquals(bookletFull.toString(), correctBookletFull);
        assertEquals(inproceedingsFull.toString(), correctInproceedingsFull);

        // Test last names
        assertEquals(bookFull.getAuthorsLastNames(), Set.of("Knuth"));
        assertEquals(bookletFull.getAuthorsLastNames(), Set.of("Knuth"));
        assertEquals(inproceedingsFull.getAuthorsLastNames(), Set.of("Oaho", "Ullman", "Yannakakis", "Oz"));
    }
}
