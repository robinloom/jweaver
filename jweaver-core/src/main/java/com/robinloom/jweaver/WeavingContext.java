package com.robinloom.jweaver;

public final class WeavingContext {

    private final Mode mode;
    private final TypeWeaverResolver typeWeaverResolver;
    private final ReflectionWeaverResolver reflectionWeaverResolver;
    private final boolean isRoot;

    public WeavingContext(Mode mode,
                          TypeWeaverResolver typeWeaverResolver,
                          ReflectionWeaverResolver reflectionWeaverResolver,
                          boolean isRoot) {
        this.mode = mode;
        this.typeWeaverResolver = typeWeaverResolver;
        this.reflectionWeaverResolver = reflectionWeaverResolver;
        this.isRoot = isRoot;
    }

    public String weave(Object value) {
        if (value == null) {
            return "null";
        }

        TypeWeaver typeWeaver = typeWeaverResolver.resolve(value.getClass());
        if (typeWeaver != null) {
            return typeWeaver.weave(value, childContext());
        }

        Weaver weaver = reflectionWeaverResolver.resolve(mode);
        return weaver.weave(value, childContext());
    }

    private WeavingContext childContext() {
        return new WeavingContext(mode, typeWeaverResolver, reflectionWeaverResolver, false);
    }

    public boolean isRoot() {
        return isRoot;
    }
}
