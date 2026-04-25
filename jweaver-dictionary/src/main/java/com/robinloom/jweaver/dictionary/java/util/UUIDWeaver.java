package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public class UUIDWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return UUID.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        UUID uuid = (UUID) object;

        return Loom.with(UUID.class.getSimpleName())
                   .lbracket()
                   .append("\"")
                   .append(uuid.toString())
                   .append("\"")
                   .rbracket()
                   .toString();
    }
}
