package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Chars;
import org.jspecify.annotations.NonNull;

public class EnumWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Enum.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Enum<?> enumObject = (Enum<?>) object;
        return object.getClass().getSimpleName() + Chars.DOT + enumObject.name();
    }
}
