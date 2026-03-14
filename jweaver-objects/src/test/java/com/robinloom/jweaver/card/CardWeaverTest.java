package com.robinloom.jweaver.card;

import com.robinloom.jweaver.JWeaver;
import com.robinloom.jweaver.Mode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CardWeaverTest {

    @Test
    public void testApiCallEqualsInternalCall() {
        record Person(String name) {}

        Person person = new Person("Peter");

        Assertions.assertEquals(JWeaver.weave(person, Mode.CARD), new CardWeaver().weave(person));
    }
}
