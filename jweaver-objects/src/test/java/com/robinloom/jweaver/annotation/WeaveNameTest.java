package com.robinloom.jweaver.annotation;

import com.robinloom.jweaver.JWeaver;
import com.robinloom.jweaver.Mode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WeaveNameTest {

    record Person(String name, @WeaveName("Mom") String nameOfTheirMother, @WeaveName("Dad") String nameOfTheirFather) {
    }

    @Test
    void testLinearWeaver() {
        WeaveNameTest.Person person = new WeaveNameTest.Person("John", "Gina", "Martin");
        String expected = "Person[name=John, Mom=Gina, Dad=Martin]";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.INLINE));
    }

    @Test
    void testTreeWeaver() {
        WeaveNameTest.Person person = new WeaveNameTest.Person("John", "Gina", "Martin");
        String expected = """
                          Person
                          |-- name=John
                          |-- Mom=Gina
                          `-- Dad=Martin""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.TREE));
    }

    @Test
    void testBulletWeaver() {
        WeaveNameTest.Person person = new WeaveNameTest.Person("John", "Gina", "Martin");
        String expected = """
                          Person
                           - name=John
                           - Mom=Gina
                           - Dad=Martin""";
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.BULLET));
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
        Assertions.assertEquals(expected, JWeaver.weave(person, Mode.CARD));
    }
}
