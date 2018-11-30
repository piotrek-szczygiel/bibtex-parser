package com.szczygiel.bibtex;

import org.testng.annotations.Test;

public class DocumentTest {
    /**
     * Basic whole file parsing test.
     */
    @Test
    public void testFileParse() {
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
                        "   author = \"Jill C. Knvth\",\n" +
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
                        "   pages = ref # \"133 139\",\n" +
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
        System.out.println(document);
    }
}
