package com.robinloom.jweaver.ast.nodes;

import java.util.ArrayList;
import java.util.List;

public sealed abstract class ReflectiveNode permits ObjectNode, PropertyNode, SequenceNode, MapEntryNode {

    protected ReflectiveNode parent;
    protected List<ReflectiveNode> children = new ArrayList<>();
    protected Integer index;

    public void addChild(ReflectiveNode child) {
        child.parent = this;
        children.add(child);
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public abstract String getHeader();

    public List<ReflectiveNode> getChildren() {
        return children;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public int getLevel() {
        if (isRoot()) {
            return 0;
        } else {
            return parent.getLevel() + 1;
        }
    }

    public boolean isLastChild() {
        return parent != null && parent.getChildren().getLast().equals(this);
    }

}
