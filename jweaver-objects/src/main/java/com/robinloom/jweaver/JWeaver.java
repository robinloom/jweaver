package com.robinloom.jweaver;

import com.robinloom.jweaver.dynamic.DynamicWeaver;

public final class JWeaver {

    private JWeaver() {}

    public static DynamicWeaver getDefault() {
        return new DynamicWeaver();
    }

}
