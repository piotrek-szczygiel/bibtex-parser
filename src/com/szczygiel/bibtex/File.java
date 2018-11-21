package com.szczygiel.bibtex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class File {
    static String read(String path) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }
}
