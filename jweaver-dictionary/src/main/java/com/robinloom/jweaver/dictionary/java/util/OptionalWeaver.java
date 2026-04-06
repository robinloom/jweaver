package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.DictionaryRegistry;
import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class OptionalWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).subclassOf(Optional.class);
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        Optional<?> optional = (Optional<?>) object;

        return optional.map(o -> {
            TypeWeaver delegate = DictionaryRegistry.find(o.getClass());
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
