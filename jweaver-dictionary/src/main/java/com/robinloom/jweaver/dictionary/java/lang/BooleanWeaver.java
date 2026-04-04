package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;
import org.jspecify.annotations.Nullable;

public class BooleanWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).exactly(Boolean.class);
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        Boolean b = (Boolean) object;
        return b ? "TRUE" : "FALSE";
    }
}
