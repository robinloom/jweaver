package com.robinloom.jweaver.tree;

import java.util.ArrayList;
import java.util.List;

class TreeNode {

    private String fieldName;
    private TreeNode parent;
    private final String value;
    private final List<TreeNode> children = new ArrayList<>();

    TreeNode(Object object) {
        this.value = object.getClass().getSimpleName();
    }

    TreeNode(String value) {
        this.value = value;
    }

    TreeNode(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    void addChild(String value) {
        addChild(null, value);
    }

    void addChild(String fieldName, String value) {
        TreeNode child = new TreeNode(fieldName, value);
        child.parent = this;
        children.add(child);
    }

    void addChild(TreeNode child) {
        child.parent = this;
        children.add(child);
    }

    boolean isRoot() {
        return parent == null;
    }

    boolean isLastChild() {
        if (isRoot()) {
            return false;
        } else {
            return parent.getChildren().getLast().equals(this);
        }
    }

    String getContent() {
        if (fieldName == null) {
            return value;
        } else {
            return fieldName + "=" + value;
        }
    }

    List<TreeNode> getChildren() {
        return children;
    }
}
