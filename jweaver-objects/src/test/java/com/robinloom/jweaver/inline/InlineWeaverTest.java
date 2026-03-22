package com.robinloom.jweaver.inline;

import com.robinloom.jweaver.JWeaver;
import com.robinloom.jweaver.Mode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

class InlineWeaverTest {

    @Test
    public void testApiCallEqualsInternalCall() {
        record Person(String name) {}

        Person person = new Person("Peter");

        Assertions.assertEquals(JWeaver.weave(person, Mode.INLINE), new InlineWeaver().weave(person));
    }

    @Test
    void testInaccessibleField() throws Exception {
        record Person(String name) {}

        InlineWeaver weaver = Mockito.spy(new InlineWeaver());

        doThrow(new IllegalAccessException("boom"))
                .when(weaver)
                .readField(any(), any());

        Person person = new Person("Peter");

        Assertions.assertEquals("Person[[?]]", weaver.weave(person));
    }
}
