package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.TimeZone;

class TimeZoneWeaverTest extends TypeWeaverTest {

    private final TimeZoneWeaver weaver = new TimeZoneWeaver();

    @Test
    void testPositiveOffset() {
        TimeZone zone = TimeZone.getTimeZone("Europe/Berlin");

        Assertions.assertEquals("TimeZone[id=Europe/Berlin, offset=+01:00]", weaver.weave(zone, ctx));
    }

    @Test
    void testNegativeOffset() {
        TimeZone zone = TimeZone.getTimeZone("America/New_York");

        Assertions.assertEquals("TimeZone[id=America/New_York, offset=-05:00]", weaver.weave(zone, ctx));
    }

    @Test
    void testZeroOffset() {
        TimeZone zone = TimeZone.getTimeZone("UTC");

        Assertions.assertEquals("TimeZone[id=UTC, offset=+00:00]", weaver.weave(zone, ctx));
    }
}
