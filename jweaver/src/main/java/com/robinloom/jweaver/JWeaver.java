package com.robinloom.jweaver;

public final class JWeaver {

    private JWeaver() {}

    public static DynamicWeaver getDefault() {
        return new DynamicWeaver();
    }

}
