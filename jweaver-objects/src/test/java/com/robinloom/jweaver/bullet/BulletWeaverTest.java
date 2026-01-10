package com.robinloom.jweaver.bullet;

import com.robinloom.jweaver.JWeaver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

class BulletWeaverTest {

    @Test
    void testNullSafety() {
        Assertions.assertEquals("null", JWeaver.Internal.bullet().weave(null));
    }

    @Test
    void testJdkClassesToString() {
        Assertions.assertEquals("Test", JWeaver.Internal.flat().weave("Test"));
        Assertions.assertEquals("[]", JWeaver.Internal.flat().weave(List.of()));
    }

    @Test
    void testSimpleWeaving() {
        record Person(String name, LocalDate birthday) {}

        Person person = new Person("John Doe", LocalDate.of(1990, 1, 1));
        String expected = """
                          Person
                           - name=John Doe
                           - birthday=1990-01-01""";

        Assertions.assertEquals(expected, JWeaver.Internal.bullet().weave(person));
    }

    @Test
    void testComplexField() {
        record Person(String name, Person neighbor) {
            @Override
            public String toString() {
                return JWeaver.Internal.bullet().weave(this);
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
        Assertions.assertEquals(expected, JWeaver.Internal.bullet().weave(person));
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
        Assertions.assertEquals(expected, JWeaver.Internal.bullet().weave(person));
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
        Assertions.assertEquals(expected, JWeaver.Internal.bullet().weave(person));
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
        Assertions.assertEquals(expected, JWeaver.Internal.bullet().weave(person));
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
        Assertions.assertEquals(expected, JWeaver.Internal.bullet().weave(person));
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

        Assertions.assertEquals(expected, JWeaver.Internal.bullet().weave(person));
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

        Assertions.assertEquals(expected, JWeaver.Internal.bullet().weave(person));
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

        Assertions.assertEquals(expected, JWeaver.Internal.bullet().weave(person));
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

        Assertions.assertEquals(expected, JWeaver.Internal.bullet().weave(person));
    }


    @Test
    void testReciprocalDependency() {
        class Twin {
            Twin twin;

            @Override
            public String toString() {
                return JWeaver.Internal.bullet().weave(this);
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
                return JWeaver.Internal.bullet().weave(this);
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
    void testFieldCapitalization() {
        record Person(String name, LocalDate birthday) {}

        Person person = new Person("Jane Doe", LocalDate.of(1990, 1, 1));
        String expected = """
                          Person
                           - Name=Jane Doe
                           - Birthday=1990-01-01""";

        Assertions.assertEquals(expected, JWeaver.Internal.bullet().capitalizeFields().weave(person));
    }

    @Test
    void testShowDataTypes() {
        record Person(String name, LocalDate birthday) {}

        Person person = new Person("Jane Doe", LocalDate.of(1990, 1, 1));
        String expected = """
                          Person
                           - {String} name=Jane Doe
                           - {LocalDate} birthday=1990-01-01""";

        Assertions.assertEquals(expected, JWeaver.Internal.bullet().showDataTypes().weave(person));
    }

    @Test
    void testExcludeField() {
        record Person(String name, char[] password) {}

        Person person = new Person("John Doe", "password".toCharArray());
        String expected = """
                          Person
                           - name=John Doe""";

        Assertions.assertEquals(expected, JWeaver.Internal.bullet().excludeFields(List.of("password")).weave(person));
    }

    @Test
    void testIncludeField() {
        record Person(String name, char[] password) {}

        Person person = new Person("John Doe", "password".toCharArray());
        String expected = """
                          Person
                           - name=John Doe""";

        Assertions.assertEquals(expected, JWeaver.Internal.bullet().includeFields(List.of("name")).weave(person));
    }

    @Test
    void testOmitClassName() {
        record Person(String name, LocalDate birthday) {}

        Person person = new Person("Jane Doe", LocalDate.of(1990, 1, 1));
        String expected = " - name=Jane Doe\n - birthday=1990-01-01";

        Assertions.assertEquals(expected, JWeaver.Internal.bullet().omitClassName().weave(person));
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
        String expected = """
                           Person
                            - name=John Doe
                            - hairColor=blonde""";

        Assertions.assertEquals(expected, JWeaver.Internal.bullet().showInheritedFields().weave(person));
    }

    @Test
    void testAlphabeticalOrder() {
        record Person(String name, LocalDate birthday) {}

        Person person = new Person("John Doe", LocalDate.of(1990, 1, 1));
        String expected = """
                          Person
                           - birthday=1990-01-01
                           - name=John Doe""";

        Assertions.assertEquals(expected, JWeaver.Internal.bullet().orderFieldsAlphabetically().weave(person));
    }

    @Test
    void testCutTreeDepth() {
        record Person(List<List<String>> listOfLists) {}

        Person person = new Person(List.of(List.of("A", "B"), List.of("C", "D")));
        String expected = """
                          Person
                           - listOfLists
                             - (0)
                             - (1)""";
        Assertions.assertEquals(expected, JWeaver.Internal.bullet().maxDepth(3).weave(person));
    }

    @Test
    void testCutLongList() {
        record Entity(List<Character> chars) {}

        Entity entity = new Entity(List.of('a', 'a', 'a', 'a', 'a'));
        String expected = """
                          Entity
                           - chars
                             - (0) a
                             - (1) a
                             - 3 more""";
        Assertions.assertEquals(expected, JWeaver.Internal.bullet().maxSequenceLength(2).weave(entity));
    }

    @Test
    void testCutLongArray() {
        record Entity(char[] chars) {}

        Entity entity = new Entity(new char[]{'a', 'a', 'a', 'a', 'a'});
        String expected = """
                          Entity
                           - chars
                             - [0] a
                             - [1] a
                             - 3 more""";
        Assertions.assertEquals(expected, JWeaver.Internal.bullet().maxSequenceLength(2).weave(entity));
    }

    @Test
    void testReusable() {
        record Car(String brand, Color color) {}
        record Person(String name, Car car) {}

        BulletWeaver weaver = JWeaver.Internal.bullet();

        Person first = new Person("Jane", new Car("Volvo", Color.BLUE));
        String firstExpected = """
                               Person
                                - name=Jane
                                - car
                                  - brand=Volvo
                                  - color=java.awt.Color[r=0,g=0,b=255]""";

        Assertions.assertEquals(firstExpected, weaver.weave(first));

        Person second = new Person("John", new Car("Audi", Color.BLACK));
        String secondExpected = """
                                Person
                                 - name=John
                                 - car
                                   - brand=Audi
                                   - color=java.awt.Color[r=0,g=0,b=0]""";

        Assertions.assertEquals(secondExpected, weaver.weave(second));
    }

    @Test
    public void testBulletCustomization() {
        record Person(int age, Person[] neighbors) {}

        Person person = new Person(40, new Person[]{new Person(15, null),
                new Person(12, null)});
        String expected = """
                          Person
                           * age=40
                           * neighbors
                             # [0] Person
                               > age=15
                             # [1] Person
                               > age=12""";

        String actual = JWeaver.Internal.bullet().firstLevelBulletChar('*')
                                           .secondLevelBulletChar('#')
                                           .deeperLevelBulletChar('>')
                               .weave(person);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testCustomIndentation() {
        record Person(String name, LocalDate birthday) {}

        Person person = new Person("John Doe", LocalDate.of(1990, 1, 1));
        String expected = """
                          Person
                             - name=John Doe
                             - birthday=1990-01-01""";

        Assertions.assertEquals(expected, JWeaver.Internal.bullet().indentation(4).weave(person));
    }
}
