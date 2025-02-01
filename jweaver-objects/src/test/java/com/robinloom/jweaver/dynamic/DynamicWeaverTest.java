package com.robinloom.jweaver.dynamic;

import com.robinloom.jweaver.JWeaver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class DynamicWeaverTest {

    @Test
    void testNullSafety() {
        Assertions.assertEquals("null", JWeaver.getDefault().weave(null));
    }

    @Test
    void testSimpleWeaving() {
        record Person(String name, LocalDate birthday) {}

        Person person = new Person("John Doe", LocalDate.of(1990, 1, 1));
        String expected = "Person[name=John Doe, birthday=1990-01-01]";

        Assertions.assertEquals(expected, JWeaver.getDefault().weave(person));
    }

    @Test
    void testComplexField() {
        record Person(String name, Person neighbor) {}

        Person person = new Person("Jane", new Person("Peter", null));
        String expected = "Person[name=Jane, neighbor=Person[name=Peter, neighbor=null]]";

        Assertions.assertEquals(expected, JWeaver.getDefault().weave(person));
    }

    @Test
    void testSimpleCollection() {
        record Person(String name, List<String> childrenNames) {}

        Person person = new Person("Jane", List.of("Peter", "Judy"));
        String expected = "Person[name=Jane, childrenNames=[Peter, Judy]]";

        Assertions.assertEquals(expected, JWeaver.getDefault().weave(person));
    }

    @Test
    void testComplexCollection() {
        record Person(String name, List<Person> neighbors) {
            @Override
            public int hashCode() { return 1; }
        }

        Person person = new Person("Jane", List.of(new Person("Peter", List.of())));
        String expected = "Person[name=Jane, neighbors=[Person[name=Peter, neighbors=[]]]]";

        Assertions.assertEquals(expected, JWeaver.getDefault().weave(person));
    }

    @Test
    void testPrimitiveArray() {
        record Person(int[] numbers) {}

        Person person = new Person(new int[]{0,1,2});
        String expected = "Person[numbers=[0,1,2]]";

        Assertions.assertEquals(expected, JWeaver.getDefault().weave(person));
    }

    @Test
    void testStringArray() {
        record Person(String[] names) {}

        Person person = new Person(new String[]{"Anna", "Maria", "Quinn"});
        String expected = "Person[names=[Anna,Maria,Quinn]]";

        Assertions.assertEquals(expected, JWeaver.getDefault().weave(person));
    }

    @Test
    void testComplexArray() {
        record Person(Person[] neighbors) {}

        Person person = new Person(new Person[]{new Person(null)});
        String expected = "Person[neighbors=[Person[neighbors=null]]]";

        Assertions.assertEquals(expected, JWeaver.getDefault().weave(person));
    }

    @Test
    void testFieldCapitalization() {
        record Person(String name, int age, boolean isTall) {}

        Person person = new Person("Jane Doe", 18, false);
        String expected = "Person[Name=Jane Doe, Age=18, IsTall=false]";

        Assertions.assertEquals(expected, JWeaver.getDefault().capitalizeFields().weave(person));
    }

    @Test
    void testCustomClassNameFormat() {
        record Person(String name, int age, boolean isTall) {}

        Person person = new Person("Jane Doe", 18, false);
        String expected = "$Person$[name=Jane Doe, age=18, isTall=false]";

        Assertions.assertEquals(expected, JWeaver.getDefault().classNamePrefix("$")
                                                              .classNameSuffix("$")
                                                              .classNameFieldsSeparator("[")
                                                              .weave(person));
    }

    @Test
    void testCustomFieldValueSeparator() {
        record Person(String name, int age, boolean isTall) {}

        Person person = new Person("Jane Doe", 18, false);
        String expected = "Person[name:Jane Doe, age:18, isTall:false]";

        Assertions.assertEquals(expected, JWeaver.getDefault().fieldValueSeparator(":").weave(person));
    }

    @Test
    void testCustomFieldSeparator() {
        record Person(String name, int age, boolean isTall) {}

        Person person = new Person("Jane Doe", 18, false);
        String expected = "Person[name=Jane Doe --- age=18 --- isTall=false]";

        Assertions.assertEquals(expected, JWeaver.getDefault().fieldSeparator(" --- ").weave(person));
    }

    @Test
    void testCustomGlobalSuffix() {
        record Person(String name, int age, boolean isTall) {}

        Person person = new Person("Jane Doe", 18, false);
        String expected = "Person(name=Jane Doe, age=18, isTall=false)";

        Assertions.assertEquals(expected, JWeaver.getDefault().classNameFieldsSeparator("(").globalSuffix(")").weave(person));
    }

    @Test
    void testShowDataTypes() {
        record Person(String name, int age, boolean isTall) {}

        Person person = new Person("Jane Doe", 18, false);
        String expected = "Person[String name=Jane Doe, int age=18, boolean isTall=false]";

        Assertions.assertEquals(expected, JWeaver.getDefault().showDataTypes().weave(person));
    }

    @Test
    void testMultilineSettings() {
        record Person(String name, int age, boolean isTall) {}

        Person person = new Person("Jane Doe", 18, false);
        String expected = """
                          Person
                          name=Jane Doe
                          age=18
                          isTall=false""";

        Assertions.assertEquals(expected, JWeaver.getDefault().multiline().weave(person));
    }

    @Test
    void testReciprocalDependency() {
        class Twin {
            Twin twin;

            @Override
            public String toString() {
                return JWeaver.getDefault().weave(this);
            }
        }

        Twin first = new Twin();
        Twin second = new Twin();
        first.twin = second;
        second.twin = first;

        Assertions.assertDoesNotThrow(first::toString);
     }

    @Test
    void testCircularDependency() {
        class Sibling {
            Sibling sibling;

            @Override
            public String toString() {
                return JWeaver.getDefault().weave(this);
            }
        }

        Sibling first = new Sibling();
        Sibling second = new Sibling();
        Sibling third = new Sibling();
        first.sibling = second;
        second.sibling = third;
        third.sibling = first;

        Assertions.assertDoesNotThrow(first::toString);
    }

    @Test
    void testExcludeField() {
        record Person(String name, char[] password) {}

        Person person = new Person("John Doe", "password".toCharArray());
        String expected = "Person[name=John Doe]";

        Assertions.assertEquals(expected, JWeaver.getDefault().excludeFields(List.of("password")).weave(person));
    }

    @Test
    void testIncludeField() {
        record Person(String name, char[] password) {}

        Person person = new Person("John Doe", "password".toCharArray());
        String expected = "Person[name=John Doe]";

        Assertions.assertEquals(expected, JWeaver.getDefault().includeFields(List.of("name")).weave(person));
    }

    @Test
    void testInheritance() {
        class Human {
            final String hairColor;

            public Human(String hairColor) {
                this.hairColor = hairColor;
            }
        }
        class Person extends Human {
            final String name;

            public Person(String name, String hairColor) {
                super(hairColor);
                this.name = name;
            }
        }

        Person person = new Person("John Doe", "blonde");
        String expected = "Person[name=John Doe, hairColor=blonde]";

        Assertions.assertEquals(expected, JWeaver.getDefault().showInheritedFields().weave(person));
    }

}
