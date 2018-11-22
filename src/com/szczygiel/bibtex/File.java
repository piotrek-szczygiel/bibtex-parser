package com.szczygiel.bibtex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Reading file into string.
 */
class File {
    static String read(String path) {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            return "";
        }

        return content;
    }
}
