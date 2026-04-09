package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MapWeaverTest extends TypeWeaverTest {

    record Person(String name) {}

    @Test
    public void testWeaveNotEmpty() {
        MapWeaver weaver = new MapWeaver();

        Map<Person, Person> people = new HashMap<>();
        people.put(new Person("John"), new Person("Frieda"));
        people.put(new Person("Margot"), new Person("Jim"));

        String expected = "HashMap(2) {Person[name=John]=Person[name=Frieda], Person[name=Margot]=Person[name=Jim]}";

        Assertions.assertEquals(expected, weaver.weave(people, context));
    }

    @Test
    public void testWeaveEmpty() {
        MapWeaver weaver = new MapWeaver();

        Map<Person, Person> people = new HashMap<>();

        String expected = "HashMap(0) {}";

        Assertions.assertEquals(expected, weaver.weave(people, context));
    }
}
