package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

public class CharacterWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Character.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Character character = (Character) object;
        return Loom.with("'").append(character).append("'").toString();
    }
}
