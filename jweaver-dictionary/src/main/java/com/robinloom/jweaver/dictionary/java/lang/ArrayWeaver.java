package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.Dictionary;
import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Sequences;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Array;

public class ArrayWeaver implements TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Object[].class;
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        int length = Array.getLength(object);
        Loom loom = Loom.with(object.getClass().getComponentType().getSimpleName());

        loom.bracket(() -> loom.append(length))
            .space()
            .bracket(() -> {
                loom.join(", ", Loom.range(0, length), i -> {
                    Object component = Array.get(object, i);

                    if (i == Sequences.SEQUENCE_LIMIT) {
                        return (".. " + (length - i) + " more");
                    } else if (i > Sequences.SEQUENCE_LIMIT) {
                        return null;
                    }

                    TypeWeaver delegate = Dictionary.find(component.getClass());

                    if (delegate != null) {
                        return delegate.weave(component, context);
                    } else {
                        return context.reflectionWeave(component);
                    }
                });
            });

        return loom.toString();
    }
}
