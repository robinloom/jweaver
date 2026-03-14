package com.robinloom.jweaver.bullet;

import com.robinloom.jweaver.JWeaver;
import com.robinloom.jweaver.Mode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BulletWeaverTest {

    @Test
    public void testApiCallEqualsInternalCall() {
        record Person(String name) {}

        Person person = new Person("Peter");

        Assertions.assertEquals(JWeaver.weave(person, Mode.BULLET), new BulletWeaver().weave(person));
    }
}
