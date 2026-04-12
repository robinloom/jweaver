package com.robinloom.jweaver.dictionary.java.nio;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class PathWeaverTest extends TypeWeaverTest {

    private final PathWeaver weaver = new PathWeaver();

    @Test
    void weave_simple_path() {
        Path path = Path.of("test/file.txt");

        String result = weaver.weave(path, ctx);

        Assertions.assertTrue(result.startsWith("Path["));
        Assertions.assertTrue(result.contains("file.txt"));
    }

    @Test
    void weave_returns_absolute_path() {
        Path path = Path.of("test.txt");

        String result = weaver.weave(path, ctx);

        Assertions.assertTrue(result.startsWith("Path["));
        Assertions.assertTrue(result.contains(path.toAbsolutePath().toString()));
    }
}
