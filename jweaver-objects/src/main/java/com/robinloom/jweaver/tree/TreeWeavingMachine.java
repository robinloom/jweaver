package com.robinloom.jweaver.tree;

import com.robinloom.jweaver.commons.WeavingMachine;

final class TreeWeavingMachine extends WeavingMachine {

    private final TreeConfig config;

    TreeWeavingMachine(TreeConfig config) {
        this.config = config;
    }

    void append(String string) {
        delegate.append(string);
    }

    void indent() {
        delegate.append(" ".repeat(4));
    }

    void indentWithCrossingBranch() {
        delegate.append("|");
        delegate.append(" ".repeat(3));
    }

    void appendBranch() {
        delegate.append(config.getBranchChar());
        delegate.append("-- ");
    }

    void appendLastBranch() {
        delegate.append(config.getLastBranchChar());
        delegate.append("-- ");
    }

    public boolean globalLimitReached() {
        return delegate.length() >= config.getGlobalLengthLimit();
    }

}