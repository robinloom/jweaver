package com.robinloom.jweaver.dictionary.java;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Sequences;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Array;

public class ArrayWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isArray();
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "";
        }

        int length = Array.getLength(object);
        Loom loom = Loom.create();

        loom.append(object.getClass().getComponentType().getSimpleName())
            .bracket(() -> loom.append(length))
            .space()
            .bracket(() -> {
                loom.join(", ", Loom.range(0, length), i -> {
                    Object component = Array.get(object, i);

                    if (i == Sequences.SEQUENCE_LIMIT) {
                        return (".. " + (length - i) + " more");
                    } else if (i > Sequences.SEQUENCE_LIMIT) {
                        return null;
                    }

                    if (component.getClass().isArray()) {
                        return weave(component, context);
                    } else {
                        return context.delegateWeave(component);
                    }
                });
            });

        return loom.toString();
    }
}
