package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClassWeaverTest extends TypeWeaverTest {

    private final ClassWeaver weaver = new ClassWeaver();

    @Test
    public void testWeaver() {
        Assertions.assertEquals("com.robinloom.jweaver.dictionary.java.lang.ClassWeaverTest",
                                weaver.weave(this.getClass(), ctx));
    }
}
