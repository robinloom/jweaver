package com.robinloom.jweaver.dictionary.java.net;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.net.URL;

public class URLWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return URL.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        return Loom.with(targetType().getSimpleName())
                   .lbracket()
                   .append(object.toString())
                   .rbracket()
                   .toString();
    }
}
