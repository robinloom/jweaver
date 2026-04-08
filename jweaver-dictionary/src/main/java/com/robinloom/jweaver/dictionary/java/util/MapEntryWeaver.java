package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.Dictionary;
import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;

import java.util.Map;

public class MapEntryWeaver implements TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Map.class;
    }

    @Override
    public String weave(Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) object;

        TypeWeaver keyDelegate = Dictionary.find(entry.getKey().getClass());
        String key = keyDelegate != null ? keyDelegate.weave(entry.getKey(), context)
                                         : context.reflectionWeave(entry.getKey());

        TypeWeaver valueDelegate = Dictionary.find(entry.getValue().getClass());
        String value = valueDelegate != null ? valueDelegate.weave(entry.getValue(), context)
                                             : context.reflectionWeave(entry.getValue());

        return key + " = " + value;
    }
}
