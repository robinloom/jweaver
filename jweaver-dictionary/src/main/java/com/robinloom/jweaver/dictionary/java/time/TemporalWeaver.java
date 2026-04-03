package com.robinloom.jweaver.dictionary.java.time;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

public class TemporalWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).subclassOf(Temporal.class);
    }

    @Override
    public String weave(Object object, WeavingContext context) {
        if (object instanceof LocalDate d) {
            return "LocalDate[" + d + "]";
        }

        if (object instanceof LocalDateTime dt) {
            return "LocalDateTime[" + dt + "]";
        }

        if (object instanceof ZonedDateTime zdt) {
            return "ZonedDateTime[" + zdt.toLocalDateTime() + zdt.getOffset() + " " + zdt.getZone() + "]";
        }

        if (object instanceof Instant i) {
            return "Instant[" + i + "]";
        }

        return object.toString();
    }
}
