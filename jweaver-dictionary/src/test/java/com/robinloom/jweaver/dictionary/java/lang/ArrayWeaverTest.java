package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArrayWeaverTest extends TypeWeaverTest {

    record Person(String name) {}

    @Test
    public void testWeaveNotEmpty() {
        ArrayWeaver weaver = new ArrayWeaver();

        Person[] people = new Person[]{new Person("John"), new Person("Jane")};

        String expected = "Person[2] [Person[name=John], Person[name=Jane]]";

        Assertions.assertEquals(expected, weaver.weave(people, context));
    }

    @Test
    public void testWeaveEmpty() {
        ArrayWeaver weaver = new ArrayWeaver();

        Person[] people = new Person[]{};

        String expected = "Person[0] []";

        Assertions.assertEquals(expected, weaver.weave(people, context));
    }

    @Test
    public void testNested() {
        ArrayWeaver weaver = new ArrayWeaver();

        Person[][] people = new Person[][]{ new Person[]{new Person("John"), new Person("Jane")}};

        String expected = "Person[][1] [Person[2] [Person[name=John], Person[name=Jane]]]";

        Assertions.assertEquals(expected, weaver.weave(people, context));
    }

    @Test
    public void testDoubleNested() {
        ArrayWeaver weaver = new ArrayWeaver();

        Person[][][] people = new Person[][][]{ new Person[][]{new Person[]{new Person("John")}, new Person[]{new Person("Jane")}}};

        String expected = "Person[][][1] [Person[][2] [Person[1] [Person[name=John]], Person[1] [Person[name=Jane]]]]";

        Assertions.assertEquals(expected, weaver.weave(people, context));
    }

    @Test
    public void testSequenceLimit() {
        ArrayWeaver weaver = new ArrayWeaver();

        int[] i = new int[] {1,2,3,4,5,6,7,8,9,10,11,12};

        String expected = "int[12] [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, .. 2 more]";

        Assertions.assertEquals(expected, weaver.weave(i, context));
    }
}
