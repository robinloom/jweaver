package com.robinloom.jweaver.util;

import java.lang.reflect.Array;

public final class TypeStrings {

    private TypeStrings() {}

    public static String array(Object o) {
        StringBuilder sb = new StringBuilder("[");
        int length = Array.getLength(o);
        for (int i = 0; i < length; i++) {
            sb.append(Array.get(o, i));
            if (i < length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
