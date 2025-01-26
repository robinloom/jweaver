package com.robinloom.jweaver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class DefaultWeaverTest {

    @Test
    void testSimpleWeaving() {
        record Person(String name, int age, boolean isTall) {}

        Person person = new Person("John Doe", 18, true);
        String expected = "Person[name=John Doe, age=18, isTall=true]";

        Assertions.assertEquals(expected, JWeaver.getDefault().weave(person));
    }

    @Test
    void testComplexField() {
        record Person(String name, Person neighbor) {}

        Person person = new Person("Jane", new Person("Peter", null));
        String expected = "Person[name=Jane, neighbor=Person@-1907803244]";

        Assertions.assertEquals(expected, new DefaultWeaver().weave(person));
    }

    @Test
    void testSimpleCollection() {
        record Person(String name, List<String> childrenNames) {}

        Person person = new Person("Jane", List.of("Peter", "Judy"));
        String expected = "Person[name=Jane, childrenNames=[Peter, Judy]]";

        Assertions.assertEquals(expected, new DefaultWeaver().weave(person));
    }

    @Test
    void testComplexCollection() {
        record Person(String name, List<Person> neighbor) {
            @Override
            public int hashCode() { return 1; }
        }

        Person person = new Person("Jane", List.of(new Person("Peter", List.of())));
        String expected = "Person[name=Jane, neighbor=[Person@1]]";

        Assertions.assertEquals(expected, new DefaultWeaver().weave(person));
    }

    @Test
    void testPrimitiveArray() {
        record Person(int[] numbers) {}

        Person person = new Person(new int[]{0,1,2});
        String expected = "Person[numbers=[0,1,2]]";

        Assertions.assertEquals(expected, new DefaultWeaver().weave(person));
    }

    @Test
    void testStringArray() {
        record Person(String[] names) {}

        Person person = new Person(new String[]{"Anna", "Maria", "Quinn"});
        String expected = "Person[names=[Anna,Maria,Quinn]]";

        Assertions.assertEquals(expected, new DefaultWeaver().weave(person));
    }

    @Test
    void testComplexArray() {
        record Person(Person[] neighbors) {}

        Person person = new Person(new Person[]{new Person(null)});
        String expected = "Person[neighbors=Person[]@%d]".formatted(person.hashCode());

        Assertions.assertEquals(expected, new DefaultWeaver().weave(person));
    }

    @Test
    void testFieldCapitalization() {
        record Person(String name, int age, boolean isTall) { }

        Person person = new Person("Jane Doe", 18, false);
        String expected = "Person[Name=Jane Doe, Age=18, IsTall=false]";

        Assertions.assertEquals(expected, new DefaultWeaver().capitalizeFields().weave(person));
    }

    @Test
    void testCustomClassNameFormat() {
        record Person(String name, int age, boolean isTall) { }

        Person person = new Person("Jane Doe", 18, false);
        String expected = "$Person$[name=Jane Doe, age=18, isTall=false]";

        Assertions.assertEquals(expected, new DefaultWeaver().classNamePrefix("$").classNameSuffix("$[").weave(person));
    }

    @Test
    void testCustomFieldValueSeparator() {
        record Person(String name, int age, boolean isTall) { }

        Person person = new Person("Jane Doe", 18, false);
        String expected = "Person[name:Jane Doe, age:18, isTall:false]";

        Assertions.assertEquals(expected, new DefaultWeaver().fieldValueSeparator(":").weave(person));
    }

    @Test
    void testCustomFieldSeparator() {
        record Person(String name, int age, boolean isTall) { }

        Person person = new Person("Jane Doe", 18, false);
        String expected = "Person[name=Jane Doe --- age=18 --- isTall=false]";

        Assertions.assertEquals(expected, new DefaultWeaver().fieldSeparator(" --- ").weave(person));
    }

    @Test
    void testCustomGlobalSuffix() {
        record Person(String name, int age, boolean isTall) { }

        Person person = new Person("Jane Doe", 18, false);
        String expected = "Person(name=Jane Doe, age=18, isTall=false)";

        Assertions.assertEquals(expected, new DefaultWeaver().classNameSuffix("(").globalSuffix(")").weave(person));
    }

}
