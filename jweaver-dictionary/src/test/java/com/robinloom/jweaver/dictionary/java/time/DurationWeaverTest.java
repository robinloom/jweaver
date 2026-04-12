package com.robinloom.jweaver.dictionary.java.time;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DurationWeaverTest extends TypeWeaverTest {

    private final DurationWeaver weaver = new DurationWeaver();

    @Test
    void weave_duration() {
        Duration d = Duration.ofHours(1).plusMinutes(23).plusSeconds(10);

        String result = weaver.weave(d, ctx);

        assertEquals("Duration[1h 23m 10s]", result);
    }
}
