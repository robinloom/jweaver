package com.robinloom.jweaver.ast;

public final class ASTOptions {

    private final boolean expandSequences;

    public ASTOptions(boolean expandSequences) {
        this.expandSequences = expandSequences;
    }

    public boolean isExpandSequences() {
        return expandSequences;
    }

    public static ASTOptions expanded() {
        return new ASTOptions(true);
    }

    public static ASTOptions compact() {
        return new ASTOptions(false);
    }
}
