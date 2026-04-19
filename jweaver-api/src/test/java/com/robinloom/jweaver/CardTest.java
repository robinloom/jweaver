package com.robinloom.jweaver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CardTest {

    @Test
    void testNullSafety() {
        Assertions.assertEquals("null", JWeaver.weave(null, Mode.CARD));
    }

    @Test
    void testSimpleWeaving() {
        record Person(String name, LocalDate birthday) {}

        Person person = new Person("John Doe", LocalDate.of(1990, 1, 1));
        String expected = """
                        ╭ Person ──────────────────────────╮
                        │ name     : John Doe              │
                        │ birthday : LocalDate[1990-01-01] │
                        ╰──────────────────────────────────╯""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.CARD));
    }

    @Test
    void testComplexField() {
        record Person(String name, Person neighbor) {
            @Override
            public int hashCode() {
                return Objects.hash(name, neighbor);
            }
        }

        Person person = new Person("Jane", new Person("Peter", null));
        String expected = """
                ╭ Person ──────────────╮
                │ name     : Jane      │
                │ neighbor : Person(2) │
                ╰──────────────────────╯""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.CARD));
    }

    @Test
    void testSimpleCollection() {
        record Person(String name, List<String> childrenNames) {}

        Person person = new Person("Jane", List.of("Peter", "Judy"));
        String expected = """
                        ╭ Person ───────────────────╮
                        │ name          : Jane      │
                        │ childrenNames : List12(2) │
                        ╰───────────────────────────╯""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.CARD));
    }

    @Test
    void testComplexCollection() {
        record Person(String name, List<Person> neighbors) {}

        Person person = new Person("Jane", List.of(new Person("Peter", List.of())));
        String expected = """
                          ╭ Person ───────────────╮
                          │ name      : Jane      │
                          │ neighbors : List12(1) │
                          ╰───────────────────────╯""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.CARD));
    }

    @Test
    void testPrimitiveArray() {
        record Person(int[] numbers) {}

        int[] arr = new  int[] {0,1,2};
        Person person = new Person(arr);
        String expected = """
                          ╭ Person ────────────╮
                          │ numbers : int[](3) │
                          ╰────────────────────╯""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.CARD));
    }

    @Test
    void testStringArray() {
        record Person(String[] names) {}

        Person person = new Person(new String[]{"Anna", "Maria", "Quinn"});
        String expected = """
                        ╭ Person ─────────────╮
                        │ names : String[](3) │
                        ╰─────────────────────╯""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.CARD));
    }

    @Test
    void testComplexArray() {
        record Person(Person[] neighbors) {}

        Person person = new Person(new Person[]{new Person(null)});
        String expected = """
                          ╭ Person ─────────────────╮
                          │ neighbors : Person[](1) │
                          ╰─────────────────────────╯""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.CARD));
    }
}
