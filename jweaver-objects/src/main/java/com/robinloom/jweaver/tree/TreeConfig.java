package com.robinloom.jweaver.tree;

import com.robinloom.jweaver.commons.WeaverConfig;

class TreeConfig extends WeaverConfig {

    private int maxDepth = 4;
    private int maxSequenceLength = 10;
    private char branchChar = '|';
    private char lastBranchChar = '`';

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getMaxSequenceLength() {
        return maxSequenceLength;
    }

    public void setMaxSequenceLength(int maxSequenceLength) {
        this.maxSequenceLength = maxSequenceLength;
    }

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
}
