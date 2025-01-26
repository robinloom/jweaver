package com.robinloom.jweaver;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.stream.Collectors;

public final class TypeStrings {

    private TypeStrings() {}

    public static String identity(Object o) {
        if (Collection.class.isAssignableFrom(o.getClass())) {
            Collection<?> c = (Collection<?>) o;
            return c.stream()
                    .map(TypeStrings::identity)
                    .collect(Collectors.joining(",", "[", "]"));
        }
        return o.getClass().getSimpleName() + "@" + o.hashCode();
    }

    public static String array(Object o) {
        StringBuilder sb = new StringBuilder("[");
        int length = Array.getLength(o);
        for (int i = 0; i < length; i++) {
            sb.append(Array.get(o, i));
            if (i < length-1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
