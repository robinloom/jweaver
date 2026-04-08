package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class MapWeaver implements TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Map.class;
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "";
        }

        Map<?, ?> map = (Map<?, ?>) object;
        MapEntryWeaver mapEntryWeaver = new MapEntryWeaver();

        Loom loom = Loom.empty();
        loom.append(map.getClass().getSimpleName())
            .paren(() -> loom.append(map.size()))
            .space()
            .lbrace()
            .join(", ", map.entrySet(), e -> mapEntryWeaver.weave(e, context))
            .rbrace();

        return loom.toString();
    }
}
