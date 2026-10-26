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
            new WeavingContext(Mode.INLINE, Dictionary.getInstance(), _ -> (object, _) -> object.toString(), false);

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
}
