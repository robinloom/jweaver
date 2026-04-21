package com.robinloom.jweaver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TreeTest {

    @Test
    void testNullSafety() {
        Assertions.assertEquals("null", JWeaver.weave(null, Mode.TREE));
    }

    @Test
    void testSimpleWeaving() {
        record Person(String name, LocalDate birthday) {}

        Person person = new Person("John Doe", LocalDate.of(1990, 1, 1));
        String expected = """
                          Person
                          |-- name="John Doe"
                          `-- birthday=LocalDate[1990-01-01]""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.TREE));
    }

    @Test
    void testComplexField() {
        record Person(String name, Person neighbor) {}

        Person person = new Person("John Doe", new Person("Peter", null));
        String expected = """
                            Person
                            |-- name="John Doe"
                            `-- neighbor=Person
                                |-- name="Peter"
                                `-- neighbor=null""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.TREE));
    }

    @Test
    void testSimpleCollection() {
        record Person(String name, List<String> childrenNames) {}

        Person person = new Person("Jane", List.of("Peter", "Judy"));
        String expected = """
                          Person
                          |-- name="Jane"
                          `-- childrenNames=List[2]
                              |-- [0] "Peter"
                              `-- [1] "Judy\"""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.TREE));
    }

    @Test
    void testResetIndentationAfterCollection() {
        record Person(String name, List<String> childrenNames, int age) {}

        Person person = new Person("Jane", List.of("Peter", "Judy"), 29);
        String expected = """
                          Person
                          |-- name="Jane"
                          |-- childrenNames=List[2]
                          |   |-- [0] "Peter"
                          |   `-- [1] "Judy"
                          `-- age=29""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.TREE));
    }

    @Test
    void testMultipleCollections() {
        record Person(String name,
                      List<String> childrenNames,
                      int age,
                      List<String> addresses,
                      String bloodType) {}

        Person person = new Person("P. Sherman", List.of("Peter", "Judy"), 29,
                                   List.of("42 Wallaby Way, Sydney"), "0+");
        String expected = """
                           Person
                           |-- name="P. Sherman"
                           |-- childrenNames=List[2]
                           |   |-- [0] "Peter"
                           |   `-- [1] "Judy"
                           |-- age=29
                           |-- addresses=List[1]
                           |   `-- [0] "42 Wallaby Way, Sydney"
                           `-- bloodType="0+\"""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.TREE));
    }

    @Test
    void testListOfLists() {
        record Person(List<List<String>> listOfLists) {}

        Person person = new Person(List.of(List.of("A", "B"), List.of("C", "D")));
        String expected = """
                            Person
                            `-- listOfLists=List[2]
                                |-- [0] List[2]
                                |   |-- [0] "A"
                                |   `-- [1] "B"
                                `-- [1] List[2]
                                    |-- [0] "C"
                                    `-- [1] "D\"""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.TREE));
    }

    @Test
    void testComplexCollection() {
        record Person(String name, List<Person> neighbors) {}

        Person person = new Person("John", List.of(new Person("Peter", List.of())));
        String expected = """
                            Person
                            |-- name="John"
                            `-- neighbors=List[1]
                                `-- [0] Person
                                    |-- name="Peter"
                                    `-- neighbors=List[0]""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.TREE));
    }

    @Test
    void testPrimitiveArray() {
        record Person(int[] numbers) {}

        Person person = new Person(new int[]{0,1,2});
        String expected = """
                          Person
                          `-- numbers=int[3]
                              |-- [0] 0
                              |-- [1] 1
                              `-- [2] 2""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.TREE));
    }

    @Test
    void testStringArray() {
        record Person(String[] names) {}

        Person person = new Person(new String[]{"Anna", "Maria", "Quinn"});
        String expected = """
                          Person
                          `-- names=String[3]
                              |-- [0] "Anna"
                              |-- [1] "Maria"
                              `-- [2] "Quinn\"""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.TREE));
    }

    @Test
    void testComplexArray() {
        record Person(int age, Person[] neighbors) {}

        Person person = new Person(40, new Person[]{new Person(15, null),
                                                         new Person(12, null)});
        String expected = """
                        Person
                        |-- age=40
                        `-- neighbors=Person[2]
                            |-- [0] Person
                            |   |-- age=15
                            |   `-- neighbors=null
                            `-- [1] Person
                                |-- age=12
                                `-- neighbors=null""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.TREE));
    }

    @Test
    void testArrayOfArrays() {
        record Person(int[][] matrix) {}

        Person person = new Person(new int[][]{ {0, 1}, {2, 3}, {4, 5}});
        String expected = """
                            Person
                            `-- matrix=int[][3]
                                |-- [0] int[2]
                                |   |-- [0] 0
                                |   `-- [1] 1
                                |-- [1] int[2]
                                |   |-- [0] 2
                                |   `-- [1] 3
                                `-- [2] int[2]
                                    |-- [0] 4
                                    `-- [1] 5""";

        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.TREE));
    }

    @Test
    void testList() {
        List<String> persons = List.of("Anna", "Maria", "Quinn");

        String expected = """
                        List[3]
                        |-- [0] "Anna"
                        |-- [1] "Maria"
                        `-- [2] "Quinn\"""";

        Assertions.assertEquals(expected,  JWeaver.weave(persons, Mode.TREE));
    }

    @Test
    void testArray() {
        String[] persons = new String[]{"Anna", "Maria", "Quinn"};

        String expected = """
                            String[3]
                            |-- [0] "Anna"
                            |-- [1] "Maria"
                            `-- [2] "Quinn\"""";

        Assertions.assertEquals(expected,  JWeaver.weave(persons, Mode.TREE));
    }

    @Test
    void testMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        String expected = """
                            Map[2]
                            |-- "key1"="value1"
                            `-- "key2"="value2\"""";

        Assertions.assertEquals(expected,  JWeaver.weave(map, Mode.TREE));
    }

    @Test
    void testMapOfLists() {
        Map<String, List<?>> map = new HashMap<>();
        map.put("key", List.of("a", "b"));

        String expected = """
                        Map[1]
                        `-- "key"
                            `-- List[2]
                                |-- [0] "a"
                                `-- [1] "b\"""";
        Assertions.assertEquals(expected, JWeaver.weave(map, Mode.TREE));
    }

    @Test
    void testObjectWithMap() {
        enum Role {
            ADMIN,
            GUEST
        }

        enum HouseHoldRole {
            ACTIVE, ADMIN
        }

        record SessionId (UUID value) {
        }

        record HouseHoldId (UUID value) {
        }

        record SessionData (UUID userId, List<Role> roles, HashMap<HouseHoldId, List<HouseHoldRole>> houseHoldRoles) {
        }

        class SessionStore {
            final ConcurrentHashMap<SessionId, SessionData> sessions  = new ConcurrentHashMap<>();
        }

        SessionStore sessionStore = new SessionStore();
        SessionId sessionId = new SessionId(UUID.fromString("ea15da83-96f3-4eff-87bf-b61f4ae36b30"));

        HashMap<HouseHoldId, List<HouseHoldRole>> houseHoldRoles = new HashMap<>();
        houseHoldRoles.put(new HouseHoldId(UUID.fromString("6679b8d6-561f-4838-95bc-f4f8bdc6568b")), List.of(HouseHoldRole.ACTIVE));
        SessionData sessionData = new SessionData(UUID.fromString("aac2d6d1-dd44-46cd-8b2f-20163d396d11"),
                                                  List.of(Role.GUEST),
                                                  houseHoldRoles);
        sessionStore.sessions.put(sessionId, sessionData);

        String expected = """
                SessionStore
                `-- sessions=Map[1]
                    `-- SessionId[value=ea15da83-96f3-4eff-87bf-b61f4ae36b30]
                        `-- SessionData
                            |-- userId=aac2d6d1-dd44-46cd-8b2f-20163d396d11
                            |-- roles=List[1]
                            |   `-- [0] Role.GUEST
                            `-- houseHoldRoles=Map[1]
                                `-- HouseHoldId[value=6679b8d6-561f-4838-95bc-f4f8bdc6568b]
                                    `-- List[1]
                                        `-- [0] HouseHoldRole.ACTIVE""";

        Assertions.assertEquals(expected, JWeaver.weave(sessionStore, Mode.TREE));
    }
}
