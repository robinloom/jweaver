package com.robinloom.jweaver.dictionary.java.net;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import org.jspecify.annotations.NonNull;

import java.net.URI;

public class URIWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return URI.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        URI uri = (URI) object;

        return targetType().getSimpleName() + "[" + uri + "]";
    }
}
