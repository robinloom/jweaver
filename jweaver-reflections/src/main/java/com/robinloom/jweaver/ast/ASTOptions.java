package com.robinloom.jweaver.ast;

public final class ASTOptions {

    private final boolean expandSequences;
    private final int maxDepth;
    private final int maxSequenceLength;

    public ASTOptions(int maxDepth, int maxSequenceLength, boolean expandSequences) {
        this.maxDepth = maxDepth;
        this.maxSequenceLength = maxSequenceLength;
        this.expandSequences = expandSequences;
    }

    public boolean isExpandSequences() {
        return expandSequences;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getMaxSequenceLength() {
        return maxSequenceLength;
    }

    public static ASTOptions expanded() {
        return new ASTOptions(4, 10, true);
    }

    public static ASTOptions compact() {
        return new ASTOptions(4, 10, false);
    }
}
