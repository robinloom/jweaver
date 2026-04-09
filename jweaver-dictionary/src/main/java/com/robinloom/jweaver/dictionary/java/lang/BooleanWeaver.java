package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import org.jspecify.annotations.NonNull;

public class BooleanWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Boolean.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Boolean b = (Boolean) object;
        return b ? "TRUE" : "FALSE";
    }
}
