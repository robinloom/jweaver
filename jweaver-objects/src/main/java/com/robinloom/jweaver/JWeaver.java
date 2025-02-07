package com.robinloom.jweaver;

import com.robinloom.jweaver.dynamic.DynamicWeaver;
import com.robinloom.jweaver.tree.TreeWeaver;

public final class JWeaver {

    private JWeaver() {}

    public static DynamicWeaver getDefault() {
        return new DynamicWeaver();
    }

    public static DynamicWeaver getDynamic() {
        return new DynamicWeaver();
    }

    public static TreeWeaver getTree() {
        return new TreeWeaver();
    }

}
