package com.robinloom.jweaver.dictionary.java.time;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemporalWeaverTest extends TypeWeaverTest {

    private final TemporalWeaver weaver = new TemporalWeaver();

    @Test
    void weave_local_date() {
        LocalDate date = LocalDate.of(2026, 4, 2);

        String result = weaver.weave(date, context);

        assertEquals("LocalDate[2026-04-02]", result);
    }

    @Test
    void weave_local_date_time() {
        LocalDateTime dt = LocalDateTime.of(2026, 4, 2, 14, 23, 10);

        String result = weaver.weave(dt, context);

        assertEquals("LocalDateTime[2026-04-02T14:23:10]", result);
    }

    @Test
    void weave_instant() {
        Instant instant = Instant.parse("2026-04-02T12:23:10Z");

        String result = weaver.weave(instant, context);

        assertEquals("Instant[2026-04-02T12:23:10Z]", result);
    }

    @Test
    void weave_zoned_date_time() {
        ZonedDateTime zdt = ZonedDateTime.of(
                2026, 4, 2, 14, 23, 10, 0,
                ZoneId.of("Europe/Berlin")
        );

        String result = weaver.weave(zdt, context);

        assertTrue(result.contains("ZonedDateTime["));
        assertTrue(result.contains("2026-04-02T14:23:10"));
        assertTrue(result.contains("Europe/Berlin"));
    }
}