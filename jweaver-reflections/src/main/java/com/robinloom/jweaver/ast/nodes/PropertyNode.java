package com.robinloom.jweaver.ast.nodes;

public final class PropertyNode extends ReflectiveNode {

    private final String fieldName;
    private final String value;

    public PropertyNode(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getHeader() {
        return value;
    }
}
