package com.robinloom.jweaver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class FieldOperations {

    private FieldOperations() {}

    public static boolean isArray(Field field) {
        Class<?> clazz = field.getType();
        return clazz.isArray();
    }

    public static List<Field> getFields(Class<?> clazz) {
        return List.of(clazz.getDeclaredFields());
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

}
