package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;
import com.robinloom.jweaver.util.Sequences;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class CollectionWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).subclassOf(Collection.class);
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        Collection<?> collection = (Collection<?>) object;

        Loom loom = Loom.with(object.getClass().getSimpleName());
        loom.paren(() -> loom.append(collection.size()))
            .space()
            .bracket(() -> {
                    AtomicInteger i = new AtomicInteger();
                    loom.join(", ", collection, item -> {
                        if (i.get() == Sequences.SEQUENCE_LIMIT) {
                            return (".. " + (collection.size() - i.getAndIncrement()) + " more");
                        } else if (i.get() > Sequences.SEQUENCE_LIMIT) {
                            i.getAndIncrement();
                            return null;
                        }

                        i.getAndIncrement();

                        if (Collection.class.isAssignableFrom(item.getClass())) {
                            return weave(item, context);
                        } else {
                            return context.delegateWeave(item);
                        }
                    });
            });

        return loom.toString();
    }
}
