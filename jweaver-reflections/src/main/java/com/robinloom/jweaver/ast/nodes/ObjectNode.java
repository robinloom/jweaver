package com.robinloom.jweaver.ast.nodes;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class ObjectNode extends ReflectiveNode implements InnerNode {

    @Nullable private final String fieldName;
    private final Class<?> clazz;

    public ObjectNode(Class<?> clazz) {
        this.clazz = clazz;
        this.fieldName = null;
    }

    public ObjectNode(String fieldName, Class<?> clazz) {
        this.fieldName = fieldName;
        this.clazz = clazz;
    }

    public @Nullable String getFieldName() {
        return fieldName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public String getHeader() {
        return getClazz().getSimpleName();
    }
}
