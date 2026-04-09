package com.robinloom.jweaver.dictionary.java.time;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.time.Duration;

public class DurationWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Duration.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Duration duration = (Duration) object;
        long seconds = duration.getSeconds();

        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;

        return Loom.with("Duration[", h, "h ", m, "m ", s, "s]").toString();
    }
}
