package com.robinloom.jweaver.dictionary;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ToStringWeaverTest {

    private final ToStringWeaver weaver = new ToStringWeaver();

    @Test
    void testTargetTypeIsObject() {
        Assertions.assertEquals(Object.class, weaver.targetType());
    }

    @Test
    void testWeaveDelegatesToToString() {
        Object value = new Object() {
            @Override
            public String toString() {
                return "custom-representation";
            }
        };

        Assertions.assertEquals("custom-representation", weaver.weave(value, null));
    }
}
