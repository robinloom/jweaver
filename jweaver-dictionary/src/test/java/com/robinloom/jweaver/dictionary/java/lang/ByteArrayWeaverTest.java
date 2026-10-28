package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ByteArrayWeaverTest extends TypeWeaverTest {

    private final ByteArrayWeaver weaver = new ByteArrayWeaver();

    @Test
    public void testWeaver() {
        Assertions.assertEquals("byte[13]: 48 65 6C 6C 6F 2C 20 77 ..",
                                weaver.weave("Hello, world!".getBytes(), ctx));
    }

    @Test
    public void testEmptyArray() {
        Assertions.assertEquals("byte[0]", weaver.weave(new byte[0], ctx));
    }

    @Test
    public void testShortArrayIsNotTruncated() {
        Assertions.assertEquals("byte[3]: 01 02 03", weaver.weave(new byte[]{1, 2, 3}, ctx));
    }
}
