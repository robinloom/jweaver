package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

class ByteArrayOutputStreamWeaverTest extends TypeWeaverTest {

    private final ByteArrayOutputStreamWeaver weaver = new ByteArrayOutputStreamWeaver();

    @Test
    public void testWeave() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.writeBytes("ABC".getBytes());

        Assertions.assertEquals("ByteArrayOutputStream[size=3]", weaver.weave(baos, context));
    }
}
