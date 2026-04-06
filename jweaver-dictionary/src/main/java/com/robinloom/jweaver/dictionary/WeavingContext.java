package com.robinloom.jweaver.dictionary;

import com.robinloom.jweaver.Weaver;

public final class WeavingContext {

    private final Weaver weaver;

    public WeavingContext(Weaver weaver) {
        this.weaver = weaver;
    }

    public String reflectionWeave(Object object) {
        return weaver.weave(object);
    }
}
