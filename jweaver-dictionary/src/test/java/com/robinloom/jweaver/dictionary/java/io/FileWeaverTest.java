package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

class FileWeaverTest extends TypeWeaverTest {

    private final FileWeaver weaver = new FileWeaver();

    @Test
    void weave_existing_file() throws Exception {
        File file = File.createTempFile("test", ".txt");

        String result = weaver.weave(file, ctx);

        Assertions.assertTrue(result.contains("File["));
        Assertions.assertTrue(result.contains("exists"));
    }
}
