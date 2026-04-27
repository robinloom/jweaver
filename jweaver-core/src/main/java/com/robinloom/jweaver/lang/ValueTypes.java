package com.robinloom.jweaver.lang;

import java.util.Currency;
import java.util.Locale;
import java.util.UUID;

public final class ValueTypes {

    private ValueTypes() {}

    public static boolean isValueType(Class<?> type) {
        if (type == null) return false;

        if (type.isPrimitive()) return true;
        if (isWrapper(type)) return true;

        if (CharSequence.class.isAssignableFrom(type)) return true;

        if (type.isEnum()) return true;

        if (Number.class.isAssignableFrom(type)) return true;

        if (isJavaTime(type)) return true;

        return type == UUID.class
                || type == Currency.class
                || type == Locale.class;
    }

    private static boolean isWrapper(Class<?> type) {
        return type == Boolean.class
                || type == Byte.class
                || type == Short.class
                || type == Integer.class
                || type == Long.class
                || type == Float.class
                || type == Double.class
                || type == Character.class;
    }

    private static boolean isJavaTime(Class<?> type) {
        return type.getPackageName().startsWith("java.time");
    }
}
