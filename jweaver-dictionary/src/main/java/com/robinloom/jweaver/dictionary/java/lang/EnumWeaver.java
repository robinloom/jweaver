package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.loom.Chars;
import org.jspecify.annotations.Nullable;

public class EnumWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isEnum();
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        Enum<?> enumObject = (Enum<?>) object;
        return object.getClass().getSimpleName() + Chars.DOT + enumObject.name();
    }
}
