package com.robinloom.jweaver.linear;

import com.robinloom.jweaver.JWeaver;
import com.robinloom.jweaver.Mode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

class LinearWeaverTest {

    @Test
    public void testApiCallEqualsInternalCall() {
        record Person(String name) {}

        Person person = new Person("Peter");

        Assertions.assertEquals(JWeaver.weave(person, Mode.INLINE), new LinearWeaver().weave(person));
    }

    @Test
    void testInaccessibleField() throws Exception {
        record Person(String name) {}

        LinearWeaver weaver = Mockito.spy(new LinearWeaver());

        doThrow(new IllegalAccessException("boom"))
                .when(weaver)
                .readField(any(), any());

        Person person = new Person("Peter");

        Assertions.assertEquals("Person[[?]]", weaver.weave(person));
        Assertions.assertEquals("""
                                Person
                                [?]
                                """, weaver.weave(person, Mode.MULTILINE));

    }
}
