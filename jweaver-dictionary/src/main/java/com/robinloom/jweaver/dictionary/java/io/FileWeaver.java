package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;

import java.io.File;

public class FileWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).exactly(java.io.File.class);
    }

    @Override
    public String weave(Object object, WeavingContext context) {
        if (object == null) {
            return "";
        }

        File file = (File) object;

        StringBuilder sb = new StringBuilder();
        sb.append("File[").append(file.getAbsolutePath());

        if (file.exists()) {
            sb.append(", exists");

            if (file.isDirectory()) {
                sb.append(", dir");
            } else if (file.isFile()) {
                sb.append(", file");
                sb.append(", ").append(formatSize(file.length()));
            }
        } else {
            sb.append(", missing");
        }

        sb.append("]");
        return sb.toString();
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        return (bytes / (1024 * 1024)) + " MB";
    }
}
