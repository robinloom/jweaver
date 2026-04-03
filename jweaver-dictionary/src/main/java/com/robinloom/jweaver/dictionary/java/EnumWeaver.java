package com.robinloom.jweaver.dictionary.java;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import org.jspecify.annotations.Nullable;

public class EnumWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isEnum();
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "";
        }

        Enum<?> enumObject = (Enum<?>) object;
        return object.getClass().getSimpleName() + "." + enumObject.name();
    }
}
