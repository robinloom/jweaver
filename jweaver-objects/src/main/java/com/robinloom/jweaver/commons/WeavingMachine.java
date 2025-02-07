package com.robinloom.jweaver.commons;

public abstract class WeavingMachine {

    protected final StringBuilder delegate;

    public WeavingMachine() {
        delegate = new StringBuilder();
    }

    public void newline() {
        delegate.append("\n");
    }

    protected void appendInaccessible() {
        delegate.append("[?]");
        newline();
    }

    protected void appendAfterException(Exception e) {
        delegate.append("[ERROR] ");
        delegate.append(e.getClass().getSimpleName());
        newline();
    }

    public String toString() {
        return delegate.toString();
    }
}
