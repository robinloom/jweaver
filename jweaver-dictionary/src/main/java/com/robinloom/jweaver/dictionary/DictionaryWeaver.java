package com.robinloom.jweaver.dictionary;

import com.robinloom.jweaver.Weaver;

public class DictionaryWeaver implements Weaver {

    private final Weaver delegate;

    public DictionaryWeaver(Weaver delegate) {
        this.delegate = delegate;
    }

    @Override
    public String weave(Object object) {
        if (object == null) {
            return "null";
        }

        TypeWeaver weaver = DictionaryRegistry.find(object.getClass());

        if (weaver != null) {
            return weaver.weave(object, new WeavingContext(delegate));
        } else {
            return delegate.weave(object);
        }
    }
}
