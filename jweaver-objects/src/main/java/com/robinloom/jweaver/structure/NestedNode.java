package com.robinloom.jweaver.structure;

import java.util.ArrayList;
import java.util.List;

public class NestedNode {

    private String fieldName;
    private NestedNode parent;
    private final String value;
    private final List<NestedNode> children = new ArrayList<>();

    public NestedNode(Object object) {
        this.value = object.getClass().getSimpleName();
    }

    NestedNode(String value) {
        this.value = value;
    }

    NestedNode(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    void addChild(String value) {
        addChild(null, value);
    }

    void addChild(String fieldName, String value) {
        NestedNode child = new NestedNode(fieldName, value);
        child.parent = this;
        children.add(child);
    }

    void addChild(NestedNode child) {
        child.parent = this;
        children.add(child);
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLastChild() {
        if (isRoot()) {
            return false;
        } else {
            return parent.getChildren().getLast().equals(this);
        }
    }

    public String getContent() {
        if (fieldName == null) {
            return value;
        } else {
            return fieldName + "=" + value;
        }
    }

    public List<NestedNode> getChildren() {
        return children;
    }

    public int getLevel() {
        if (isRoot()) {
            return 0;
        } else {
            return parent.getLevel() + 1;
        }
    }
}
