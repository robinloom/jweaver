package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.Dictionary;
import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class OptionalWeaver implements TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Optional.class;
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        Optional<?> optional = (Optional<?>) object;

        return optional.map(o -> {
            TypeWeaver delegate = Dictionary.find(o.getClass());
            if (delegate != null) {
                return Loom.with("Optional")
                           .lparen()
                           .append(delegate.weave(o, context))
                           .rparen()
                           .toString();
            } else {
                return Loom.with("Optional")
                        .lparen()
                        .append(context.reflectionWeave(o))
                        .rparen()
                        .toString();
            }
        }).orElse("Optional()");
    }
}
