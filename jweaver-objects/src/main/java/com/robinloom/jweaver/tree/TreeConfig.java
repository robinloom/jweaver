package com.robinloom.jweaver.tree;

import com.robinloom.jweaver.commons.Properties;
import com.robinloom.jweaver.commons.WeaverConfig;

class TreeConfig extends WeaverConfig {

    private char branchChar;
    private char lastBranchChar;

    public TreeConfig() {
        branchChar = Properties.TREE_BRANCH_CHAR.getChar('|');
        lastBranchChar = Properties.TREE_BRANCH_CHAR.getChar('`');
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
