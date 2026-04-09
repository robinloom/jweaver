package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return InputStream.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        InputStream in = (InputStream) object;
        String className = in.getClass().getSimpleName();
        try {
            return Loom.with(className,
                             "[remaining=", in.available(),
                            "]")
                       .toString();
        } catch (IOException e) {
            return className + "[]";
        }
    }
}
