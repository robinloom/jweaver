package com.robinloom.jweaver.dictionary.java.net;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
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

        return Loom.with(targetType().getSimpleName())
                   .lbracket()
                   .append(uri.toString())
                   .rbracket()
                   .toString();
    }
}
