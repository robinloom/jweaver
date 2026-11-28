package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.Mode;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.jweaver.dictionary.Dictionary;
import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringWeaverTest extends TypeWeaverTest {

    private final StringWeaver weaver = new StringWeaver();
    private final WeavingContext nested =
            new WeavingContext(Mode.INLINE, Dictionary.getInstance(), unused -> (object, unusedCtx) -> object.toString(), false);

    @Test
    public void testWeaver() {
        Assertions.assertEquals("@@@", weaver.weave("@@@", ctx));
    }

    @Test
    public void testStringBuilderIsHandledLikeAString() {
        Assertions.assertEquals("hello", weaver.weave(new StringBuilder("hello"), ctx));
        Assertions.assertEquals("\"hello\"", weaver.weave(new StringBuilder("hello"), nested));
    }

    @Test
    public void testStringBufferIsHandledLikeAString() {
        Assertions.assertEquals("hello", weaver.weave(new StringBuffer("hello"), ctx));
        Assertions.assertEquals("\"hello\"", weaver.weave(new StringBuffer("hello"), nested));
    }

    @Test
    public void testNestedStringAtTheLimitIsNotCut() {
        String s = "a".repeat(StringWeaver.MAX_LENGTH);

        Assertions.assertEquals("\"" + s + "\"", weaver.weave(s, nested));
    }

    @Test
    public void testNestedStringOverTheLimitIsCutAndAnnotated() {
        String s = "a".repeat(StringWeaver.MAX_LENGTH + 500);

        Assertions.assertEquals(
                "\"" + "a".repeat(StringWeaver.MAX_LENGTH) + "...\" (" + s.length() + " chars)",
                weaver.weave(s, nested));
    }

    @Test
    public void testRootContextStringIsNeverCut() {
        String s = "a".repeat(StringWeaver.MAX_LENGTH * 3);

        Assertions.assertEquals(s, weaver.weave(s, ctx));
    }

    @Test
    public void testLongStringBuilderIsCutLikeAString() {
        StringBuilder sb = new StringBuilder("b".repeat(StringWeaver.MAX_LENGTH + 1));

        Assertions.assertEquals(
                "\"" + "b".repeat(StringWeaver.MAX_LENGTH) + "...\" (" + sb.length() + " chars)",
                weaver.weave(sb, nested));
    }

    @Test
    public void testSurrogatePairIsNotSplit() {
        // emoji spans the cut position
        String s = "a".repeat(StringWeaver.MAX_LENGTH - 1) + "\uD83D\uDE00" + "tail";

        Assertions.assertEquals(
                "\"" + "a".repeat(StringWeaver.MAX_LENGTH - 1) + "...\" (" + s.length() + " chars)",
                weaver.weave(s, nested));
    }
}
