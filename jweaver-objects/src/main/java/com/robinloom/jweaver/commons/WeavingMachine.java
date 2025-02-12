package com.robinloom.jweaver.commons;

public abstract class WeavingMachine {

    protected final StringBuilder delegate;

    public WeavingMachine() {
        delegate = new StringBuilder();
    }

    public void newline() {
        delegate.append("\n");
    }

    public void space() {
        delegate.append(" ");
    }

    public String toString() {
        String result = delegate.toString();
        delegate.setLength(0);
        return result;
    }
}
