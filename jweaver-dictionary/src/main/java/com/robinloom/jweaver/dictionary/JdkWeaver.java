package com.robinloom.jweaver.dictionary;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import org.jspecify.annotations.NonNull;

public class JdkWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Object.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        return object.toString();
    }
}
