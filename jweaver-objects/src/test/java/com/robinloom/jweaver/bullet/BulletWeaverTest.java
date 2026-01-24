package com.robinloom.jweaver.bullet;

import com.robinloom.jweaver.JWeaver;
import com.robinloom.jweaver.Mode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

class BulletWeaverTest {

    @Test
    void testNullSafety() {
        Assertions.assertEquals("null", JWeaver.weave(null, Mode.BULLET));
    }

    @Test
    void testJdkClassesToString() {
        Assertions.assertEquals("Test", JWeaver.weave("Test", Mode.BULLET));
        Assertions.assertEquals("[]", JWeaver.weave(List.of(), Mode.BULLET));
    }

    @Test
    void testSimpleWeaving() {
        record Person(String name, LocalDate birthday) {}

        Person person = new Person("John Doe", LocalDate.of(1990, 1, 1));
        String expected = """
                          Person
                           - name=John Doe
                           - birthday=1990-01-01""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.BULLET));
    }

    @Test
    void testComplexField() {
        record Person(String name, Person neighbor) {
            @Override
            public String toString() {
                return JWeaver.weave(this, Mode.BULLET);
            }
        }

        Person person = new Person("John Doe", new Person("Peter", null));
        String expected = """
                          Person
                           - name=John Doe
                           - neighbor
                             - name=Peter""";
        Assertions.assertEquals(expected, person.toString());
    }

    @Test
    void testSimpleCollection() {
        record Person(String name, List<String> childrenNames) {}

        Person person = new Person("Jane", List.of("Peter", "Judy"));
        String expected = """
                          Person
                           - name=Jane
                           - childrenNames
                             - (0) Peter
                             - (1) Judy""";
        Assertions.assertEquals(expected, JWeaver.weave(person));
    }

    @Test
    void testResetIndentationAfterCollection() {
        record Person(String name, List<String> childrenNames, int age) {}

        Person person = new Person("Jane", List.of("Peter", "Judy"), 29);
        String expected = """
                          Person
                           - name=Jane
                           - childrenNames
                             - (0) Peter
                             - (1) Judy
                           - age=29""";
        Assertions.assertEquals(expected, JWeaver.weave(person));
    }

    @Test
    void testMultipleCollections() {
        record Person(String name,
                      List<String> childrenNames,
                      int age,
                      List<String> addresses,
                      String bloodType) {}

        Person person = new Person("Jane", List.of("Peter", "Judy"), 29,
                List.of("42 Wallaby Way, Sydney"), "0+");
        String expected = """
                           Person
                            - name=Jane
                            - childrenNames
                              - (0) Peter
                              - (1) Judy
                            - age=29
                            - addresses
                              - (0) 42 Wallaby Way, Sydney
                            - bloodType=0+""";
        Assertions.assertEquals(expected, JWeaver.weave(person));
    }

    @Test
    void testListOfLists() {
        record Person(List<List<String>> listOfLists) {}

        Person person = new Person(List.of(List.of("A", "B"), List.of("C", "D")));
        String expected = """
                          Person
                           - listOfLists
                             - (0)
                               - (0) A
                               - (1) B
                             - (1)
                               - (0) C
                               - (1) D""";
        Assertions.assertEquals(expected, JWeaver.weave(person));
    }

    @Test
    void testComplexCollection() {
        record Person(String name, List<Person> neighbors) {
            @Override
            public int hashCode() { return 1; }
        }

        Person person = new Person("John", List.of(new Person("Peter", List.of())));
        String expected = """
                          Person
                           - name=John
                           - neighbors
                             - (0) Person
                               - name=Peter
                               - neighbors""";
        Assertions.assertEquals(expected, JWeaver.weave(person));
    }

    @Test
    void testPrimitiveArray() {
        record Person(int[] numbers) {}

        Person person = new Person(new int[]{0,1,2});
        String expected = """
                          Person
                           - numbers
                             - [0] 0
                             - [1] 1
                             - [2] 2""";

        Assertions.assertEquals(expected, JWeaver.weave(person));
    }

    @Test
    void testStringArray() {
        record Person(String[] names) {}

        Person person = new Person(new String[]{"Anna", "Maria", "Quinn"});
        String expected = """
                          Person
                           - names
                             - [0] Anna
                             - [1] Maria
                             - [2] Quinn""";

        Assertions.assertEquals(expected, JWeaver.weave(person));
    }

    @Test
    void testComplexArray() {
        record Person(int age, Person[] neighbors) {}

        Person person = new Person(40, new Person[]{new Person(15, null),
                new Person(12, null)});
        String expected = """
                          Person
                           - age=40
                           - neighbors
                             - [0] Person
                               - age=15
                             - [1] Person
                               - age=12""";

        Assertions.assertEquals(expected, JWeaver.weave(person));
    }

    @Test
    void testArrayOfArrays() {
        record Person(int[][] matrix) {}

        Person person = new Person(new int[][]{ {0, 1}, {2, 3}, {4, 5}});
        String expected = """
                          Person
                           - matrix
                             - [0]
                               - [0] 0
                               - [1] 1
                             - [1]
                               - [0] 2
                               - [1] 3
                             - [2]
                               - [0] 4
                               - [1] 5""";

        Assertions.assertEquals(expected, JWeaver.weave(person));
    }


    @Test
    void testReciprocalDependency() {
        class Twin {
            Twin twin;

            @Override
            public String toString() {
                return JWeaver.weave(this);
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
                return JWeaver.weave(this);
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
