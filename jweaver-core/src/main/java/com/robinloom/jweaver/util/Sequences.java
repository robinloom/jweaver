package com.robinloom.jweaver.util;

import com.robinloom.jweaver.Weaver;
import org.jspecify.annotations.Nullable;

public final class Sequences {

    public static final int SEQUENCE_LIMIT = 10;

    public static String arrayToString(@Nullable Object object, Weaver weaver) {
        if (object == null)
            return "null";

        Object[] array = (Object[]) object;

        int iMax = array.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            if (array[i].getClass().isArray()) {
                b.append(arrayToString(array[i], weaver));
            } else {
                b.append(weaver.weave(array[i]));
            }
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
