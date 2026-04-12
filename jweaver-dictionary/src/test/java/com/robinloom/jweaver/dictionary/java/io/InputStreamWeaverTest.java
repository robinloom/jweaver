package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

class InputStreamWeaverTest extends TypeWeaverTest {

    private final InputStreamWeaver weaver = new InputStreamWeaver();

    @Test
    public void testWeave() {
        ByteArrayInputStream bais = new ByteArrayInputStream("ABC".getBytes());

        Assertions.assertEquals("ByteArrayInputStream[remaining=3]", weaver.weave(bais, ctx));
    }
}
