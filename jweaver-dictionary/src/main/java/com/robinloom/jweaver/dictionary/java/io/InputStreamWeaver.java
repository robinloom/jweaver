package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;
import com.robinloom.loom.Loom;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).subclassOf(InputStream.class);
    }

    @Override
    public String weave(Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

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
