package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;
import com.robinloom.loom.Loom;

import java.io.File;

public class FileWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).exactly(java.io.File.class);
    }

    @Override
    public String weave(Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        File file = (File) object;

        return Loom.with("File[", file.getAbsolutePath())
                   .appendIf(file.exists(), ", exists", ", missing")
                   .appendIf(file.isDirectory(), ", dir")
                   .appendIf(file.isFile(), ", file, ")
                   .appendIf(file.isFile(), formatSize(file.length()))
                   .rbracket()
                   .toString();
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        return (bytes / (1024 * 1024)) + " MB";
    }
}
