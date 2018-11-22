package com.szczygiel.bibtex;

/**
 * Main program class.
 */
public class Main {
    public static void main(String[] args) {
        Document document = new Document();
        if (document.load("test1.bib")) {
            document.parse();
            System.out.println(document);
        }
    }
}
