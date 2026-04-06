package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.DictionaryRegistry;
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

        TypeWeaver keyDelegate = DictionaryRegistry.find(entry.getKey().getClass());
        String key = keyDelegate != null ? keyDelegate.weave(entry.getKey(), context)
                                         : context.reflectionWeave(entry.getKey());

        TypeWeaver valueDelegate = DictionaryRegistry.find(entry.getValue().getClass());
        String value = valueDelegate != null ? valueDelegate.weave(entry.getValue(), context)
                                             : context.reflectionWeave(entry.getValue());

        return key + " = " + value;
    }
}
