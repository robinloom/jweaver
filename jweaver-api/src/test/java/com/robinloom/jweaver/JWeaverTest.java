package com.robinloom.jweaver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JWeaverTest {

    @Test
    void switchMode() {
        record Person(String name) {}

        JWeaver.switchMode(Mode.TREE);

        String expected = """
                          Person
                          `-- name="John\"""";

        assertEquals(expected, JWeaver.weave(new Person("John")));
    }
}