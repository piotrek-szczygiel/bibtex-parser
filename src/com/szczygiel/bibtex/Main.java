package com.szczygiel.bibtex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static String readFile(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public static void main(String[] args) {
        Parser parser = new Parser();
        String input;

//        input = readFile("file1.bib");
//        parser.parse(input);

        input = readFile("file2.bib");
        parser.parse(input);

    }
}
