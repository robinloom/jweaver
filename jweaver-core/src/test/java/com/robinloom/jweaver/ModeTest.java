package com.robinloom.jweaver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ModeTest {

    @Test
    void testValuesContainsAllModes() {
        Assertions.assertArrayEquals(new Mode[]{Mode.INLINE, Mode.TREE}, Mode.values());
    }

    @Test
    void testValueOfRoundTrips() {
        Assertions.assertEquals(Mode.INLINE, Mode.valueOf("INLINE"));
        Assertions.assertEquals(Mode.TREE, Mode.valueOf("TREE"));
    }
}
