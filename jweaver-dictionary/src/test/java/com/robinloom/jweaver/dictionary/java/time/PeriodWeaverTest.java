package com.robinloom.jweaver.dictionary.java.time;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Test;

import java.time.Period;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PeriodWeaverTest extends TypeWeaverTest {

    PeriodWeaver weaver = new PeriodWeaver();

    @Test
    void weave_period() {
        Period p = Period.of(2, 3, 5);

        String result = weaver.weave(p, ctx);

        assertEquals("Period[2y 3m 5d]", result);
    }
}
