package com.robinloom.jweaver.tree;

import com.robinloom.jweaver.commons.WeaverConfig;

class TreeConfig extends WeaverConfig {

    private char branchChar = '|';
    private char lastBranchChar = '`';
    private int maxDepth = 4;

    public char getBranchChar() {
        return branchChar;
    }

    public void setBranchChar(char branchChar) {
        this.branchChar = branchChar;
    }

    public char getLastBranchChar() {
        return lastBranchChar;
    }

    public void setLastBranchChar(char lastBranchChar) {
        this.lastBranchChar = lastBranchChar;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
}
