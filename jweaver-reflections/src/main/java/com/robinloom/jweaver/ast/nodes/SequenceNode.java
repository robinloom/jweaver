package com.robinloom.jweaver.ast.nodes;

public final class SequenceNode extends ReflectiveNode implements InnerNode {

    private final String fieldName;
    private final Class<?> clazz;
    private final Integer size;

    public SequenceNode(String fieldName, Class<?> clazz, Integer size) {
        this.fieldName = fieldName;
        this.clazz = clazz;
        this.size = size;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getClassName() {
        return clazz.getSimpleName();
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Integer getSize() {
        return size;
    }

    @Override
    public String getHeader() {
        return getClassName();
    }
}
