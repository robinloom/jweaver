package com.robinloom.jweaver.dictionary.java.time;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

import java.time.Duration;

public class DurationWeaver implements TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Duration.class;
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        Duration duration = (Duration) object;
        long seconds = duration.getSeconds();

        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;

        return Loom.with("Duration[", h, "h ", m, "m ", s, "s]").toString();
    }
}
