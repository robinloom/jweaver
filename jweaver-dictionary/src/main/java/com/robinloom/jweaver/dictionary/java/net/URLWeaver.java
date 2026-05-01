package com.robinloom.jweaver.dictionary.java.net;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import org.jspecify.annotations.NonNull;

import java.net.URL;

public class URLWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return URL.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        URL url = (URL) object;

        return targetType().getSimpleName() + "[" + url + "]";
    }
}
