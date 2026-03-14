package com.robinloom.jweaver.tree;

import com.robinloom.jweaver.JWeaver;
import com.robinloom.jweaver.Mode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TreeWeaverTest {

    @Test
    public void testApiCallEqualsInternalCall() {
        record Person(String name) {}

        Person person = new Person("Peter");

        Assertions.assertEquals(JWeaver.weave(person, Mode.TREE), new TreeWeaver().weave(person));
    }
}
