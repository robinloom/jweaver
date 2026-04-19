package com.robinloom.jweaver.ast.nodes;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class PropertyNode extends ReflectiveNode {

    @Nullable private final String fieldName;
    private final String value;

    public PropertyNode(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    public PropertyNode(String value) {
        this.fieldName = null;
        this.value = value;
    }

    public @Nullable String getFieldName() {
        return fieldName;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getHeader() {
        return value;
    }

    @Override
    public String toString() {
        if (fieldName == null) {
            return value;
        } else {
            return fieldName + "=" + value;
        }
    }
}
