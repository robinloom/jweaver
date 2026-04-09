package com.robinloom.jweaver;

public final class WeavingContext {

    private final Mode mode;
    private final TypeWeaverResolver typeWeaverResolver;
    private final ReflectionWeaverResolver reflectionWeaverResolver;

    public WeavingContext(Mode mode,
                          TypeWeaverResolver typeWeaverResolver,
                          ReflectionWeaverResolver reflectionWeaverResolver) {
        this.mode = mode;
        this.typeWeaverResolver = typeWeaverResolver;
        this.reflectionWeaverResolver = reflectionWeaverResolver;
    }

    public String weave(Object value) {
        if (value == null) {
            return "null";
        }

        TypeWeaver typeWeaver = typeWeaverResolver.resolve(value.getClass());
        if (typeWeaver != null) {
            return typeWeaver.weave(value, this);
        }

        Weaver weaver = reflectionWeaverResolver.resolve(mode);
        return weaver.weave(value, this);
    }
}
