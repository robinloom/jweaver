package com.robinloom.jweaver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BulletTest {

    @Test
    void testNullSafety() {
        Assertions.assertEquals("null", JWeaver.weave(null, Mode.BULLET));
    }

    @Test
    void testSimpleWeaving() {
        record Person(String name, LocalDate birthday) {}

        Person person = new Person("John Doe", LocalDate.of(1990, 1, 1));
        String expected = """
                          Person
                              - name=John Doe
                              - birthday=LocalDate[1990-01-01]""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.BULLET));
    }

    @Test
    void testComplexField() {
        record Person(String name, Person neighbor) {}

        Person person = new Person("John Doe", new Person("Peter", null));
        String expected = """
                          Person
                              - name=John Doe
                              - neighbor=Person
                                  - name=Peter
                                  - neighbor=null""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.BULLET));
    }

    @Test
    void testSimpleCollection() {
        record Person(String name, List<String> childrenNames) {}

        Person person = new Person("Jane", List.of("Peter", "Judy"));
        String expected = """
                          Person
                              - name=Jane
                              - childrenNames=List12[2]
                                  - [0] Peter
                                  - [1] Judy""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.BULLET));
    }

    @Test
    void testResetIndentationAfterCollection() {
        record Person(String name, List<String> childrenNames, int age) {}

        Person person = new Person("Jane", List.of("Peter", "Judy"), 29);
        String expected = """
                          Person
                              - name=Jane
                              - childrenNames=List12[2]
                                  - [0] Peter
                                  - [1] Judy
                              - age=29""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.BULLET));
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
                               - childrenNames=List12[2]
                                   - [0] Peter
                                   - [1] Judy
                               - age=29
                               - addresses=List12[1]
                                   - [0] 42 Wallaby Way, Sydney
                               - bloodType=0+""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.BULLET));
    }

    @Test
    void testListOfLists() {
        record Person(List<List<String>> listOfLists) {}

        Person person = new Person(List.of(List.of("A", "B"), List.of("C", "D")));
        String expected = """
                            Person
                                - listOfLists=List12[2]
                                    - [0] List12[2]
                                        - [0] A
                                        - [1] B
                                    - [1] List12[2]
                                        - [0] C
                                        - [1] D""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.BULLET));
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
                                - neighbors=List12[1]
                                    - [0] Person
                                        - name=Peter
                                        - neighbors=ListN[0]""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.BULLET));
    }

    @Test
    void testPrimitiveArray() {
        record Person(int[] numbers) {}

        Person person = new Person(new int[]{0,1,2});
        String expected = """
                          Person
                              - numbers=int[][3]
                                  - [0] 0
                                  - [1] 1
                                  - [2] 2""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.BULLET));
    }

    @Test
    void testStringArray() {
        record Person(String[] names) {}

        Person person = new Person(new String[]{"Anna", "Maria", "Quinn"});
        String expected = """
                          Person
                              - names=String[][3]
                                  - [0] Anna
                                  - [1] Maria
                                  - [2] Quinn""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.BULLET));
    }

    @Test
    void testComplexArray() {
        record Person(int age, Person[] neighbors) {}

        Person person = new Person(40, new Person[]{new Person(15, null),
                new Person(12, null)});
        String expected = """
                            Person
                                - age=40
                                - neighbors=Person[][2]
                                    - [0] Person
                                        - age=15
                                        - neighbors=null
                                    - [1] Person
                                        - age=12
                                        - neighbors=null""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.BULLET));
    }

    @Test
    void testArrayOfArrays() {
        record Person(int[][] matrix) {}

        Person person = new Person(new int[][]{ {0, 1}, {2, 3}, {4, 5}});
        String expected = """
                            Person
                                - matrix=int[][][3]
                                    - [0] int[][2]
                                        - [0] 0
                                        - [1] 1
                                    - [1] int[][2]
                                        - [0] 2
                                        - [1] 3
                                    - [2] int[][2]
                                        - [0] 4
                                        - [1] 5""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.BULLET));
    }


    @Test
    void testReciprocalDependency() {
        class Twin {
            Twin twin;

            @Override
            public String toString() {
                return JWeaver.weave(this, Mode.BULLET);
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


    @Test
    void testCollection() {
        List<String> persons = List.of("Anna", "Maria", "Quinn");

        String expected = """
                        ListN
                            - [0] Anna
                            - [1] Maria
                            - [2] Quinn""";

        Assertions.assertEquals(expected,  JWeaver.weave(persons, Mode.BULLET));
    }

    @Test
    void testArray() {
        String[] persons = new String[]{"Anna", "Maria", "Quinn"};

        String expected = """
                            String[]
                                - [0] Anna
                                - [1] Maria
                                - [2] Quinn""";

        Assertions.assertEquals(expected,  JWeaver.weave(persons, Mode.BULLET));
    }

    @Test
    void testMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        String expected = """
                            HashMap
                                - key1=value1
                                - key2=value2""";

        Assertions.assertEquals(expected,  JWeaver.weave(map, Mode.BULLET));
    }
}
