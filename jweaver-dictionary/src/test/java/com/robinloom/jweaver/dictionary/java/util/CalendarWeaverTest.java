package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

class CalendarWeaverTest extends TypeWeaverTest {

    private final CalendarWeaver weaver = new CalendarWeaver();

    @Test
    void testWeave() {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.set(2026, Calendar.OCTOBER, 26, 12, 0, 0);

        String result = weaver.weave(calendar, ctx);

        Assertions.assertEquals("GregorianCalendar[time=2026-10-26T12:00:00Z, zone=UTC]", result);
    }
}
