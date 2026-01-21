package com.robinloom.jweaver.annotation;

import com.robinloom.jweaver.JWeaver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WeaveNameTest {

    record Person(String name, @WeaveName("Mom") String nameOfTheirMother, @WeaveName("Dad") String nameOfTheirFather) {
    }

    @Test
    void testDynamicWeaver() {
        WeaveNameTest.Person person = new WeaveNameTest.Person("John", "Gina", "Martin");
        String expected = "Person[name=John, Mom=Gina, Dad=Martin]";
        Assertions.assertEquals(expected, JWeaver.Advanced.flat().weave(person));
    }

    @Test
    void testTreeWeaver() {
        WeaveNameTest.Person person = new WeaveNameTest.Person("John", "Gina", "Martin");
        String expected = """
                          Person
                          |-- name=John
                          |-- Mom=Gina
                          `-- Dad=Martin""";
        Assertions.assertEquals(expected, JWeaver.Advanced.tree().weave(person));
    }

    @Test
    void testBulletWeaver() {
        WeaveNameTest.Person person = new WeaveNameTest.Person("John", "Gina", "Martin");
        String expected = """
                          Person
                           - name=John
                           - Mom=Gina
                           - Dad=Martin""";
        Assertions.assertEquals(expected, JWeaver.Advanced.bullet().weave(person));
    }

    @Test
    void testCardWeaver() {
        WeaveNameTest.Person person = new WeaveNameTest.Person("John", "Gina", "Martin");
        String expected = """
                         ╭ Person ───────╮
                         │ name : John   │
                         │ Mom  : Gina   │
                         │ Dad  : Martin │
                         ╰───────────────╯""";
        Assertions.assertEquals(expected, JWeaver.Advanced.card().weave(person));
    }
}
