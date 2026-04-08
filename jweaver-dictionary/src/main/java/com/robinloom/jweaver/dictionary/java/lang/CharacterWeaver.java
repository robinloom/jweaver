package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

public class CharacterWeaver implements TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Character.class;
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        Character character = (Character) object;
        return Loom.with("'").append(character).append("'").toString();
    }
}
