package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class CollectionWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Collection.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Collection<?> collection = (Collection<?>) object;

        Loom loom = Loom.with(object.getClass().getSimpleName());
        loom.paren(() -> loom.append(collection.size()))
            .space()
            .bracket(() -> {
                    AtomicInteger i = new AtomicInteger();
                    loom.join(", ", collection, item -> {
                        if (i.get() == 10) {
                            return (".. " + (collection.size() - i.getAndIncrement()) + " more");
                        } else if (i.get() > 10) {
                            i.getAndIncrement();
                            return null;
                        }

                        i.getAndIncrement();

                        return ctx.weave(item);
                    });
            });

        return loom.toString();
    }
}
