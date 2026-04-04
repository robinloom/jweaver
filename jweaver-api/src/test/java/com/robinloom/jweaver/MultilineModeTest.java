package com.robinloom.jweaver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class MultilineModeTest {

    @Test
    void testNullSafety() {
        Assertions.assertEquals("null", JWeaver.weave(null, Mode.MULTILINE));
    }

    @Test
    void testRecordWeaving() {
        record Person(String name, LocalDate birthday) {}

        Person person = new Person("John Doe", LocalDate.of(1990, 1, 1));
        String expected = """
                          Person
                          name: John Doe
                          birthday: 1990-01-01""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.MULTILINE));
    }

    @Test
    void testNestedField() {
        record Person(String name, Person neighbor) {}

        Person person = new Person("Jane", new Person("Peter", null));
        String expected = """
                          Person
                          name: Jane
                          neighbor: Person[name=Peter, neighbor=null]""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.MULTILINE));
    }

    @Test
    void testNestedDifferentField() {
        class Car {
            final String color;

            public Car(String color) {
                this.color = color;
            }

            @Override
            public int hashCode() {
                return 1;
            }
        }

        record Person(String name, Car car) {}

        Person person = new Person("Jane", new Car("Black"));
        String expected = """
                          Person
                          name: Jane
                          car: com.robinloom.jweaver.MultilineModeTest$1Car@1""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.MULTILINE));
    }

    @Test
    void testSimpleCollection() {
        record Person(String name, List<String> childrenNames) {}

        Person person = new Person("Jane", List.of("Peter", "Judy"));
        String expected = """
                          Person
                          name: Jane
                          childrenNames: [Peter, Judy]""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.MULTILINE));
    }

    @Test
    void testComplexCollection() {
        record Person(String name, List<Person> neighbors) {}

        Person person = new Person("Jane", List.of(new Person("Peter", List.of())));
        String expected = """
                          Person
                          name: Jane
                          neighbors: [Person[name=Peter, neighbors=[]]]""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.MULTILINE));
    }

    @Test
    void testPrimitiveArray() {
        record Person(int[] numbers) {}

        Person person = new Person(new int[]{0,1,2});
        String expected = """
                          Person
                          numbers: [0, 1, 2]""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.MULTILINE));
    }

    @Test
    void testStringArray() {
        record Person(String[] names) {}

        Person person = new Person(new String[]{"Anna", "Maria", "Quinn"});
        String expected = """
                          Person
                          names: [Anna, Maria, Quinn]""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.MULTILINE));
    }

    @Test
    void testComplexArray() {
        record Person(Person[] neighbors) {}

        Person person = new Person(new Person[]{new Person(null)});
        String expected = """
                          Person
                          neighbors: [Person[neighbors=null]]""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.MULTILINE));
    }

    @Test
    void testReciprocalDependency() {
        class Twin {
            Twin twin;

            @Override
            public String toString() {
                return JWeaver.weave(this, Mode.MULTILINE);
            }
        }

        Twin first = new Twin();
        Twin second = new Twin();
        first.twin = second;
        second.twin = first;

        Assertions.assertDoesNotThrow(first::toString);
        Assertions.assertDoesNotThrow(second::toString);
    }

    @Test
    void testCircularDependency() {
        class Sibling {
            Sibling sibling;

            @Override
            public String toString() {
                return JWeaver.weave(this, Mode.MULTILINE);
            }
        }

        Sibling first = new Sibling();
        Sibling second = new Sibling();
        Sibling third = new Sibling();
        first.sibling = second;
        second.sibling = third;
        third.sibling = first;

        Assertions.assertDoesNotThrow(first::toString);
        Assertions.assertDoesNotThrow(second::toString);
        Assertions.assertDoesNotThrow(third::toString);
    }
}
