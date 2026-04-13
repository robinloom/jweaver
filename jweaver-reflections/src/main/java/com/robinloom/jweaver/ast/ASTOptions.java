package com.robinloom.jweaver.ast;

public final class ASTOptions {

    private final int maxDepth;
    private final int maxSequenceLength;

    public ASTOptions(int maxDepth, int maxSequenceLength) {
        this.maxDepth = maxDepth;
        this.maxSequenceLength = maxSequenceLength;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getMaxSequenceLength() {
        return maxSequenceLength;
    }

    public static ASTOptions defaultOptions() {
        return new ASTOptions(4, 10);
    }
}
