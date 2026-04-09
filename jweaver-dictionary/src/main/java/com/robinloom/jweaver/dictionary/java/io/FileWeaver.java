package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.io.File;

public class FileWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return java.io.File.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
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
