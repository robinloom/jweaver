package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

import java.io.ByteArrayOutputStream;

public class ByteArrayOutputStreamWeaver implements TypeWeaver {

    @Override
    public Class<?> targetType() {
        return ByteArrayOutputStream.class;
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        ByteArrayOutputStream out = (ByteArrayOutputStream) object;
        return Loom.with("ByteArrayOutputStream[size=", out.size(), "]").toString();
    }
}
