package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class MapWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Map.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Map<?, ?> map = (Map<?, ?>) object;

        Loom loom = Loom.empty();
        loom.append(map.getClass().getSimpleName())
            .paren(() -> loom.append(map.size()))
            .space()
            .lbrace()
            .join(", ", map.entrySet(), ctx::weave)
            .rbrace();

        return loom.toString();
    }
}
