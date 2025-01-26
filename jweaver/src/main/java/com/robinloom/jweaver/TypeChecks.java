package com.robinloom.jweaver;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Set;

public final class TypeChecks {

    private TypeChecks() {}

    public static boolean isLogFriendly(Field field) {
        Class<?> clazz = field.getType();
        return clazz.isPrimitive() || isCharSequence(clazz) ||
               isBoxedPrimitive(clazz) || isLogFriendlyCollection(field);
    }

    private static boolean isCharSequence(Class<?> clazz) {
        return CharSequence.class.isAssignableFrom(clazz);
    }

    private static boolean isBoxedPrimitive(Class<?> clazz) {
        return Set.of(Boolean.class, Byte.class, Character.class, Double.class,
                      Integer.class, Short.class, Long.class, Float.class).contains(clazz);
    }

    private static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    static boolean isLogFriendlyCollection(Field field) {
        if (isCollection(field.getType())) {
            try {
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                Class<?> genericClazz = (Class<?>) genericType.getActualTypeArguments()[0];

                return genericClazz.isPrimitive() || isCharSequence(genericClazz) || isBoxedPrimitive(genericClazz);
            } catch (ClassCastException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isLogFriendlyArray(Field field) {
        Class<?> clazz = field.getType().getComponentType();
        if (clazz != null) {
            return clazz.isPrimitive() || isCharSequence(clazz) || isBoxedPrimitive(clazz);
        }
        return false;
    }

}
