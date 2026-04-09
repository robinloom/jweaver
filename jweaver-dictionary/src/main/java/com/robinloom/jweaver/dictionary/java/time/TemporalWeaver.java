package com.robinloom.jweaver.dictionary.java.time;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

public class TemporalWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Temporal.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        return switch (object) {
            case LocalDate d -> Loom.with(LocalDate.class.getSimpleName())
                    .lbracket()
                    .append(d)
                    .rbracket()
                    .toString();
            case LocalDateTime dt -> Loom.with(LocalDateTime.class.getSimpleName())
                    .lbracket()
                    .append(dt)
                    .rbracket()
                    .toString();
            case ZonedDateTime zdt -> Loom.with(ZonedDateTime.class.getSimpleName())
                    .lbracket()
                    .append(zdt.toLocalDateTime())
                    .append(zdt.getOffset())
                    .space()
                    .append(zdt.getZone())
                    .rbracket()
                    .toString();
            case Instant i -> Loom.with(Instant.class.getSimpleName())
                    .lbracket()
                    .append(i)
                    .rbracket()
                    .toString();
            default -> object.toString();
        };

    }
}
