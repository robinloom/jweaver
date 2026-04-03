package com.robinloom.jweaver.dictionary.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnumWeaverTest extends TypeWeaverTest {

    private enum Status {
        OPEN,
        CLOSED
    }

    private final EnumWeaver weaver = new EnumWeaver();

    @Test
    public void testEnumWeaver() {
        Assertions.assertEquals("Status.OPEN", weaver.weave(Status.OPEN, context));
        Assertions.assertEquals("Status.CLOSED", weaver.weave(Status.CLOSED, context));
    }
}
