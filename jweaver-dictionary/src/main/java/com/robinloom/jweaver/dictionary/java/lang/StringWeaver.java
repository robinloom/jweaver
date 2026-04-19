package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import org.jspecify.annotations.NonNull;

public class StringWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return String.class;
    }

    @Override
    public String weave(@NonNull Object value, WeavingContext ctx) {
        String s = (String) value;

        if (ctx.isRoot()) {
            return s;
        }

        return "\"" + s + "\"";
    }
}
