package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.loom.Chars;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

public class ByteArrayWeaver implements TypeWeaver {

    @Override
    public Class<?> targetType() {
        return byte[].class;
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        byte[] bytes = (byte[]) object;

        if (bytes.length == 0) {
            return "byte[0]";
        }

        Loom loom = Loom.with("byte[", bytes.length).append("]: ");

        int limit = Math.min(bytes.length, 8);

        for (int i = 0; i < limit; i++) {
            loom.append(String.format("%02X", bytes[i]));
            if (i < limit - 1) {
                loom.space();
            }
        }

        if (bytes.length > limit) {
            loom.space().append(Chars.repeat(Chars.DOT, 2));
        }

        return loom.toString();
    }
}
