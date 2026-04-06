package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.dictionary.DictionaryRegistry;
import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;
import com.robinloom.jweaver.util.Sequences;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Array;

public class ArrayWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isArray() && !Classes.is(clazz).exactly(byte[].class);
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

                    TypeWeaver delegate = DictionaryRegistry.find(component.getClass());

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
