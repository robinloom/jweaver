package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.Dictionary;
import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Sequences;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class CollectionWeaver implements TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Collection.class;
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

                        TypeWeaver delegate = Dictionary.find(item.getClass());
                        if (delegate != null) {
                            return weave(item, context);
                        } else {
                            return context.reflectionWeave(item);
                        }
                    });
            });

        return loom.toString();
    }
}
