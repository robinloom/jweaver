package com.robinloom.jweaver.dictionary.java.time;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;
import com.robinloom.loom.Loom;

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
            return Loom.with(LocalDate.class.getSimpleName())
                       .lbracket()
                       .append(d)
                       .rbracket()
                       .toString();
        }

        if (object instanceof LocalDateTime dt) {
            return Loom.with(LocalDateTime.class.getSimpleName())
                       .lbracket()
                       .append(dt)
                       .rbracket()
                       .toString();
        }

        if (object instanceof ZonedDateTime zdt) {
            return Loom.with(ZonedDateTime.class.getSimpleName())
                       .lbracket()
                       .append(zdt.toLocalDateTime())
                       .append(zdt.getOffset())
                       .space()
                       .append(zdt.getZone())
                       .rbracket()
                       .toString();
        }

        if (object instanceof Instant i) {
            return Loom.with(Instant.class.getSimpleName())
                       .lbracket()
                       .append(i)
                       .rbracket()
                       .toString();
        }

        return object.toString();
    }
}
