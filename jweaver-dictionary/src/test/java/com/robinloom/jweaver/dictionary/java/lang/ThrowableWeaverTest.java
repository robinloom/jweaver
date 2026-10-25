package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ThrowableWeaverTest extends TypeWeaverTest {

    private final ThrowableWeaver weaver = new ThrowableWeaver();

    @Test
    void testMessageAndFrameWithoutCause() {
        IllegalStateException e = new IllegalStateException("boom");

        String result = weaver.weave(e, ctx);

        Assertions.assertTrue(result.startsWith("IllegalStateException: boom (at "));
        Assertions.assertTrue(result.contains("ThrowableWeaverTest.testMessageAndFrameWithoutCause"));
        Assertions.assertFalse(result.contains("Caused by"));
    }

    @Test
    void testCauseChainIncludesBothFrames() {
        IllegalStateException inner = new IllegalStateException("inner failure");
        RuntimeException outer = new RuntimeException("outer failure", inner);

        String result = weaver.weave(outer, ctx);

        Assertions.assertTrue(result.startsWith("RuntimeException: outer failure (at "));
        Assertions.assertTrue(result.contains("[Caused by: IllegalStateException: inner failure (at "));
        Assertions.assertTrue(result.endsWith(")]"));
    }

    @Test
    void testMissingMessageOmitsColon() {
        NullPointerException npe = new NullPointerException();

        String result = weaver.weave(npe, ctx);

        Assertions.assertTrue(result.startsWith("NullPointerException ("));
        Assertions.assertFalse(result.contains(": null"));
    }

    @Test
    void testDefaultCauseIsSelfAndOmitted() {
        RuntimeException e = new RuntimeException("x");

        String result = weaver.weave(e, ctx);

        Assertions.assertFalse(result.contains("Caused by"));
    }

    @Test
    void testCycleBetweenTwoCausesTerminates() {
        RuntimeException a = new RuntimeException("a");
        RuntimeException b = new RuntimeException("b", a);
        a.initCause(b);

        String result = weaver.weave(a, ctx);

        Assertions.assertTrue(result.contains("[Caused by: ...]"));
    }

    @Test
    void testChainDeeperThanMaxChainIsTruncated() {
        Throwable current = new RuntimeException("level 0");
        for (int i = 1; i < 12; i++) {
            current = new RuntimeException("level " + i, current);
        }

        String result = weaver.weave(current, ctx);

        Assertions.assertTrue(result.contains("[Caused by: ...]"));
        Assertions.assertFalse(result.contains("level 0"));
    }
}
