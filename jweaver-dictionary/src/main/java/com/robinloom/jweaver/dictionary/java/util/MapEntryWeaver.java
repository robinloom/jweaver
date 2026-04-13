package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class MapEntryWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Map.Entry.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) object;

        String key = ctx.weave(entry.getKey());
        String value = ctx.weave(entry.getValue());

        return key + " = " + value;
    }
}
