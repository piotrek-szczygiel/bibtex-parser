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
        // write your code here
        String input = readFile("file1.bib");

        Parser parser = new Parser();
        parser.parse(input);

    }
}
