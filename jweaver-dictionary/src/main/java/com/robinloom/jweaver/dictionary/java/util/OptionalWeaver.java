package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class OptionalWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Optional.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Optional<?> optional = (Optional<?>) object;

        return optional.map(o -> Loom.with("Optional")
                .lparen()
                .append(ctx.weave(o))
                .rparen()
                .toString()).orElse("Optional()");
    }
}
