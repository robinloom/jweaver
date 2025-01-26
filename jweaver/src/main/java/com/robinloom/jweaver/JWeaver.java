package com.robinloom.jweaver;

public final class JWeaver {

    private JWeaver() {}

    public static DefaultWeaver getDefault() {
        return new DefaultWeaver();
    }

}
