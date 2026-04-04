package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BooleanWeaverTest extends TypeWeaverTest {

    private final BooleanWeaver weaver = new BooleanWeaver();

    @Test
    public void testWeaver() {
        Assertions.assertEquals("TRUE", weaver.weave(Boolean.TRUE,  context));
        Assertions.assertEquals("FALSE", weaver.weave(Boolean.FALSE,  context));
    }
}
