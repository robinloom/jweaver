package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;
import org.jspecify.annotations.Nullable;

public class StringWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).subclassOf(String.class);
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        return (String) object;
    }
}
