package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CharacterWeaverTest extends TypeWeaverTest {

    private final CharacterWeaver weaver = new CharacterWeaver();

    @Test
    public void testWeaver() {
        Assertions.assertEquals("'@'", weaver.weave('@', context));
    }
}
