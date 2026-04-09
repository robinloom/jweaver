package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

public class ClassWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Class.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Class<?> clazz = (Class<?>) object;

        return Loom.with(clazz.getName()).toString();
    }
}
