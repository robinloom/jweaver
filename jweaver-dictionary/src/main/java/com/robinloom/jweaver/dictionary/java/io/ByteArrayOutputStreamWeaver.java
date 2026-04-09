package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.io.ByteArrayOutputStream;

public class ByteArrayOutputStreamWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return ByteArrayOutputStream.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext context) {
        ByteArrayOutputStream out = (ByteArrayOutputStream) object;
        return Loom.with("ByteArrayOutputStream[size=", out.size(), "]").toString();
    }
}
