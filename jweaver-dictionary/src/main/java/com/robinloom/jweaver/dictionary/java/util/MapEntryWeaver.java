package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;

import java.util.Map;

public class MapEntryWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> type) {
        return Classes.is(type).subclassOf(Map.class);
    }

    @Override
    public String weave(Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) object;
        Object key = entry.getKey();
        Object value = entry.getValue();

        return context.delegateWeave(key) + " = " + context.delegateWeave(value);
    }
}
