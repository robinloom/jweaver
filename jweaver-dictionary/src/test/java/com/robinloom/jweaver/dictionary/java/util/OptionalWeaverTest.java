package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class OptionalWeaverTest extends TypeWeaverTest {

    record Person(String name) {}

    @Test
    public void testWeavePresent() {
        OptionalWeaver weaver = new OptionalWeaver();

        Optional<Person> person = Optional.of(new Person("John"));

        String expected = "Optional(Person[name=John])";

        Assertions.assertEquals(expected, weaver.weave(person, ctx));
    }

    @Test
    public void testWeaveNotPresent() {
        OptionalWeaver weaver = new OptionalWeaver();

        String expected = "Optional()";

        Assertions.assertEquals(expected, weaver.weave(Optional.empty(), ctx));
    }
}

