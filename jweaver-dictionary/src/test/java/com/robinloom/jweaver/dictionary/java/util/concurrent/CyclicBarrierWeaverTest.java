package com.robinloom.jweaver.dictionary.java.util.concurrent;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CyclicBarrier;

class CyclicBarrierWeaverTest extends TypeWeaverTest {

    private final CyclicBarrierWeaver weaver = new CyclicBarrierWeaver();

    @Test
    void testWeave() {
        CyclicBarrier barrier = new CyclicBarrier(3);

        Assertions.assertEquals("CyclicBarrier[parties=3, waiting=0]", weaver.weave(barrier, ctx));
    }
}
