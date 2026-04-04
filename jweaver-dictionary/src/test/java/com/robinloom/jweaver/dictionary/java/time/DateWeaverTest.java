package com.robinloom.jweaver.dictionary.java.time;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import com.robinloom.jweaver.dictionary.java.util.DateWeaver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

class DateWeaverTest extends TypeWeaverTest {

    private final DateWeaver weaver = new DateWeaver();

    @Test
    public void testWeaver() {
        Assertions.assertEquals("Date[1970-01-01T00:00:00Z]", weaver.weave(new Date(0), context));
    }
}
