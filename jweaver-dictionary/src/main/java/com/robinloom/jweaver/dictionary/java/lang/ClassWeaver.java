package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

public class ClassWeaver implements TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Class.class;
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        Class<?> clazz = (Class<?>) object;

        return Loom.with(clazz.getName()).toString();
    }
}
