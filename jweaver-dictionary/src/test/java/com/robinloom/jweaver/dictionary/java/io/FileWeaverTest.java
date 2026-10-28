package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Files;

class FileWeaverTest extends TypeWeaverTest {

    private final FileWeaver weaver = new FileWeaver();

    @Test
    void weave_existing_file() throws Exception {
        File file = File.createTempFile("test", ".txt");

        String result = weaver.weave(file, ctx);

        Assertions.assertTrue(result.contains("File["));
        Assertions.assertTrue(result.contains("exists"));
    }

    @Test
    void weave_missing_file() {
        File file = new File("/this/path/should/not/exist-" + System.nanoTime());

        String result = weaver.weave(file, ctx);

        Assertions.assertTrue(result.contains("missing"));
        Assertions.assertFalse(result.contains("dir"));
        Assertions.assertFalse(result.contains(", file,"));
    }

    @Test
    void weave_directory() throws Exception {
        File dir = Files.createTempDirectory("jweaver-test").toFile();

        String result = weaver.weave(dir, ctx);

        Assertions.assertTrue(result.contains("exists"));
        Assertions.assertTrue(result.contains(", dir"));
    }

    @Test
    void weave_file_size_in_kilobytes() throws Exception {
        File file = File.createTempFile("test", ".txt");
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(2048);
        }

        String result = weaver.weave(file, ctx);

        Assertions.assertTrue(result.contains("2 KB"));
    }

    @Test
    void weave_file_size_in_megabytes() throws Exception {
        File file = File.createTempFile("test", ".txt");
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.setLength(2L * 1024 * 1024);
        }

        String result = weaver.weave(file, ctx);

        Assertions.assertTrue(result.contains("2 MB"));
    }
}
