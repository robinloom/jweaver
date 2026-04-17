package com.robinloom.jweaver.ast.nodes;

public final class SequenceItemNode extends ReflectiveNode {

    private final String value;
    private final Integer index;

    public SequenceItemNode(String value, Integer index) {
        this.value = value;
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String getHeader() {
        return String.valueOf(index);
    }


}
