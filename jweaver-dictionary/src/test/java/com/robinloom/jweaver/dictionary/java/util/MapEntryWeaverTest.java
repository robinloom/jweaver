package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class MapEntryWeaverTest extends TypeWeaverTest {

    record Person(String name) {}

    record Car(String color) {}

    @Test
    public void testWeave() {
        MapEntryWeaver weaver = new MapEntryWeaver();

        Map<Person, Car> map = new HashMap<>();
        map.put(new Person("John"), new Car("red"));

        Map.Entry<?, ?> entry = map.entrySet().iterator().next();

        String expected = "Person[name=John] = Car[color=red]";

        Assertions.assertEquals(expected, weaver.weave(entry, ctx));
    }
}
