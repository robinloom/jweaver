package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

class InputStreamWeaverTest extends TypeWeaverTest {

    private final InputStreamWeaver weaver = new InputStreamWeaver();

    @Test
    public void testWeave() {
        ByteArrayInputStream bais = new ByteArrayInputStream("ABC".getBytes());

        Assertions.assertEquals("ByteArrayInputStream[remaining=3]", weaver.weave(bais, ctx));
    }

    @Test
    public void testWeaveHandlesIOExceptionFromAvailable() {
        class BrokenInputStream extends InputStream {
            @Override
            public int read() throws IOException {
                throw new IOException("boom");
            }

            @Override
            public int available() throws IOException {
                throw new IOException("boom");
            }
        }

        Assertions.assertEquals("BrokenInputStream[]", weaver.weave(new BrokenInputStream(), ctx));
    }
}
