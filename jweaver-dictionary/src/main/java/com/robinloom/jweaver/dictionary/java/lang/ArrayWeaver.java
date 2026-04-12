package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.Mode;
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
        if (ctx.getMode() == Mode.INLINE) {
            return weaveForInline(object, ctx);
        } else {
            return weaverForMultiline(object, ctx);
        }
    }

    private String weaveForInline(Object object, WeavingContext ctx) {
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

    private String weaverForMultiline(@NonNull Object object, WeavingContext ctx) {
        int length = Array.getLength(object);
        Loom loom = Loom.with(object.getClass().getComponentType().getSimpleName());

        loom.lbracket().append(length).rbracket();
        loom.newlines(2);
        for (int i = 0; i < length; i++) {
            loom.lbracket().append(i).rbracket();
            loom.newline();

            Object component = Array.get(object, i);
            loom.append(ctx.weave(component));
            loom.newlines(2);
        }

        loom.removeLastNewline().removeLastNewline();
        return loom.toString();
    }
}
