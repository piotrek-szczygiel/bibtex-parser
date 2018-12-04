package com.szczygiel.bibtex;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Tests for whole {@link Document} parsing.
 */
public class DocumentTest {
    /**
     * Field validation test.
     * <p>
     * This test checks if exception is thrown (as it should).
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateMissingYear() {
        // Missing year
        String fileStr = "@BOOK{book-full,\n" +
                "   author = \"Knuth| Donald E. \",\n" +
                "   title = \"Seminumerical Algorithms\",\n" +
                "   volume = 2,\n" +
                "   series = \"The Art of Computer Programming\",\n" +
                "   publisher = \"Addison-Wesley\",\n" +
                "   address = \"Reading Massachusetts\",\n" +
                "   edition = \"Second\",\n" +
                "   month = \"10 \" # \"styczeń\",\n" +
                "   note = \"This is a full BOOK entry\"\n" +
                "}";

        Document document = new Document();
        document.loadString(fileStr);
        document.parse();
    }

    /**
     * Same as {@link #testValidateMissingYear}.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateMissingTitle() {
        // Missing title
        String fileStr = "@MANUAL{manual-full,\n" +
                "   author = \"Larry Manmaker\",\n" +
                "   organization = \"Chips-R-Us\",\n" +
                "   address = \"Silicon Valley\",\n" +
                "   edition = \"Silver\",\n" +
                "   month = \"kwiecień\",\n" +
                "   year = 1986,\n" +
                "   note = \"This is a full MANUAL entry\"\n" +
                "}";

        Document document = new Document();
        document.loadString(fileStr);
        document.parse();
    }


    /**
     * Entry type validation test.
     * <p>
     * This test checks if exception is thrown (as it should).
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testValidateInvalidEntryType() {
        String fileStr = "@INVALID_ENTRY_TYPE{some_citation_key,\n" +
                "    author = \"Piotr Szczygieł\",\n" +
                "    note = \"This entry has invalid entry type.\"\n" +
                "}";

        Document document = new Document();
        document.loadString(fileStr);
        document.parse();
    }

    /**
     * Test if validation removes ignored fields.
     */
    @Test
    public void testValidateRemoveIgnored() {
        String fileStr = "@INPROCEEDINGS{inproceedings-full,\n" +
                "   author = \"Alfred V. Oaho and Jeffrey D. Ullman and Yannakakis| Mihalis \",\n" +
                "   title = \"On Notions of Information Transfer in {VLSI} Circuits\",\n" +
                "   editor = \"Wizard V. Oz and Mihalis Yannakakis\",\n" +
                "   booktitle = \"Proc. Fifteenth Annual ACM STOC\",\n" +
                "   number = 17,\n" +
                "   series = \"All ACM Conferences\",\n" +
                "   ignore = \"this\",\n" +
                "   pages = \"133 139\",\n" +
                "   month = \"marzec\",\n" +
                "   year = 1983,\n" +
                "   address = \"Boston\",\n" +
                "   organization = \"ACM\",\n" +
                "   publisher = \"Academic Press\",\n" +
                "   note = \"This is a full INPROCEDINGS entry\"\n" +
                "}";

        String correctOutput = "inproceedings-full(inproceedings): \n" +
                "\tauthor(string): Alfred V. Oaho and Jeffrey D. Ullman and Yannakakis| Mihalis \n" +
                "\ttitle(string): On Notions of Information Transfer in {VLSI} Circuits\n" +
                "\tbooktitle(string): Proc. Fifteenth Annual ACM STOC\n" +
                "\tyear(number): 1983\n" +
                "\teditor(string): Wizard V. Oz and Mihalis Yannakakis\n" +
                "\tseries(string): All ACM Conferences\n" +
                "\tpages(string): 133 139\n" +
                "\taddress(string): Boston\n" +
                "\tmonth(string): marzec\n" +
                "\torganization(string): ACM\n" +
                "\tpublisher(string): Academic Press\n" +
                "\tnote(string): This is a full INPROCEDINGS entry\n\n";

        Document document = new Document();
        document.loadString(fileStr);
        document.parse();

        assertEquals(document.toString(), correctOutput);
    }

    /**
     * Test if crossreference is working properly.
     */
    @Test
    public void testCrossreference() {
        String fileStr = "@STRING{\n" +
                "    jan = \"styczeń\",\n" +
                "    may = \"maj\"\n" +
                "}\n" +
                "\n" +
                "@BOOK{book-full,\n" +
                "   author = \"Knuth| Donald E. and Piotr Szczygieł\",\n" +
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
                "@BOOK{book-full-2,\n" +
                "   crossref = \"book-full\",\n" +
                "   edition = \"Third\",\n" +
                "   month = \"14 \" # may,\n" +
                "   year = 1982,\n" +
                "}\n";

        String correctOutput = "book-full(book): \n" +
                "\tauthor(string): Knuth| Donald E. and Piotr Szczygieł\n" +
                "\ttitle(string): Seminumerical Algorithms\n" +
                "\tpublisher(string): Addison-Wesley\n" +
                "\tyear(number): 1981\n" +
                "\tvolume(number): 2\n" +
                "\tseries(string): The Art of Computer Programming\n" +
                "\taddress(string): Reading Massachusetts\n" +
                "\tedition(string): Second\n" +
                "\tmonth(string): 10 styczeń\n" +
                "\tnote(string): This is a full BOOK entry\n" +
                "\n" +
                "book-full-2(book): \n" +
                "\tcrossref(string): book-full\n" +
                "\tauthor(string): Knuth| Donald E. and Piotr Szczygieł\n" +
                "\ttitle(string): Seminumerical Algorithms\n" +
                "\tpublisher(string): Addison-Wesley\n" +
                "\tyear(number): 1982\n" +
                "\tvolume(number): 2\n" +
                "\tseries(string): The Art of Computer Programming\n" +
                "\taddress(string): Reading Massachusetts\n" +
                "\tedition(string): Third\n" +
                "\tmonth(string): 14 maj\n" +
                "\tnote(string): This is a full BOOK entry\n\n";

        Document document = new Document();
        document.loadString(fileStr);
        document.parse();

        assertEquals(document.toString(), correctOutput);
    }
}
