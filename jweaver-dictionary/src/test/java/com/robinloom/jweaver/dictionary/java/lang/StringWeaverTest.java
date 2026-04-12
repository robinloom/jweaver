package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringWeaverTest extends TypeWeaverTest {

    private final StringWeaver weaver = new StringWeaver();

    @Test
    public void testWeaver() {
        Assertions.assertEquals("@@@", weaver.weave("@@@", ctx));
    }
}
