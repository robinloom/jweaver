package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class UUIDWeaverTest extends TypeWeaverTest {

    private final UUIDWeaver weaver = new UUIDWeaver();

    @Test
    void testWeave() {
        String expected = "UUID[\"00000000-0000-0000-0000-000000000000\"]";
        UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");

        Assertions.assertEquals(expected, weaver.weave(uuid, ctx));
    }
}