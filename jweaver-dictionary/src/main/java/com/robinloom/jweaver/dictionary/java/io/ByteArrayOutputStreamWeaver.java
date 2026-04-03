package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;
import org.jspecify.annotations.Nullable;

import java.io.ByteArrayOutputStream;

public class ByteArrayOutputStreamWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).subclassOf(ByteArrayOutputStream.class);
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "";
        }

        ByteArrayOutputStream out = (ByteArrayOutputStream) object;
        return "ByteArrayOutputStream[size=" + out.size() + "]";
    }
}
