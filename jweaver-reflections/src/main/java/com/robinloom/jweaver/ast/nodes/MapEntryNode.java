package com.robinloom.jweaver.ast.nodes;

public final class MapEntryNode extends ReflectiveNode {

    private final String key;

    public MapEntryNode(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getHeader() {
        return "";
    }
}
