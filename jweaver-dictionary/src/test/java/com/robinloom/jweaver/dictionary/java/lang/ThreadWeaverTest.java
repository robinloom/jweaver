package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ThreadWeaverTest extends TypeWeaverTest {

    private final ThreadWeaver weaver = new ThreadWeaver();

    @Test
    void testWeave() {
        Thread thread = new Thread(() -> {}, "worker-1");
        thread.setPriority(7);

        String result = weaver.weave(thread, ctx);

        Assertions.assertTrue(result.startsWith("Thread[name=\"worker-1\", state="));
        Assertions.assertTrue(result.contains("priority=7"));
        Assertions.assertTrue(result.endsWith("]"));
    }

    @Test
    void testCurrentThreadIsRunnable() {
        String result = weaver.weave(Thread.currentThread(), ctx);

        Assertions.assertTrue(result.contains("state=State.RUNNABLE"));
    }
}
