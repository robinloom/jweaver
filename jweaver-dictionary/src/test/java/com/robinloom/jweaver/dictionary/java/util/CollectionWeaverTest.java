package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionWeaverTest extends TypeWeaverTest {

    record Person(String name) {}

    @Test
    public void testWeaveNotEmpty() {
        CollectionWeaver weaver = new CollectionWeaver();

        ArrayList<Person> people = new ArrayList<>();
        people.add(new Person("John"));
        people.add(new Person("Jane"));

        String expected = "ArrayList(2) [Person[name=John], Person[name=Jane]]";

        Assertions.assertEquals(expected, weaver.weave(people, context));
    }

    @Test
    public void testWeaveEmpty() {
        CollectionWeaver weaver = new CollectionWeaver();

        ArrayList<Person> people = new ArrayList<>();

        String expected = "ArrayList(0) []";

        Assertions.assertEquals(expected, weaver.weave(people, context));
    }

    @Test
    public void testList() {
        CollectionWeaver weaver = new CollectionWeaver();

        List<Person> people = List.of(new Person("John"), new Person("Jane"));

        String expected = "List12(2) [Person[name=John], Person[name=Jane]]";

        Assertions.assertEquals(expected, weaver.weave(people, context));
    }

    @Test
    public void testSet() {
        CollectionWeaver weaver = new CollectionWeaver();

        Set<Person> people = new HashSet<>();
        people.add(new Person("John"));
        people.add(new Person("Jane"));

        String expected = "HashSet(2) [Person[name=John], Person[name=Jane]]";

        Assertions.assertEquals(expected, weaver.weave(people, context));
    }

    @Test
    public void testNested() {
        CollectionWeaver weaver = new CollectionWeaver();

        List<List<Person>> people = List.of(List.of(new Person("John")), List.of(new Person("Jane")));

        String expected = "List12(2) [List12(1) [Person[name=John]], List12(1) [Person[name=Jane]]]";

        Assertions.assertEquals(expected, weaver.weave(people, context));
    }

    @Test
    public void testSequenceLimit() {
        CollectionWeaver weaver = new CollectionWeaver();

        List<Integer> i = new ArrayList<>();
        i.add(1);
        i.add(2);
        i.add(3);
        i.add(4);
        i.add(5);
        i.add(6);
        i.add(7);
        i.add(8);
        i.add(9);
        i.add(10);
        i.add(11);
        i.add(12);

        String expected = "ArrayList(12) [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, .. 2 more]";

        Assertions.assertEquals(expected, weaver.weave(i, context));
    }
}
