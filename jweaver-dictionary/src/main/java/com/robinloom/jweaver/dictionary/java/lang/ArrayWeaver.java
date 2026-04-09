package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Array;

public class ArrayWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Object[].class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        int length = Array.getLength(object);
        Loom loom = Loom.with(object.getClass().getComponentType().getSimpleName());

        loom.bracket(() -> loom.append(length))
            .space()
            .bracket(() -> loom.join(", ", Loom.range(0, length), i -> {
                Object component = Array.get(object, i);

                if (i == 10) {
                    return (".. " + (length - i) + " more");
                } else if (i > 10) {
                    return null;
                }

                return ctx.weave(component);
            }));

        return loom.toString();
    }
}
