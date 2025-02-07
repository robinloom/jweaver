package com.robinloom.jweaver.util;

import java.util.Collection;

public final class TypeDictionary {

    private TypeDictionary() {}

    public static boolean isSimpleType(Class<?> clazz) {
        return (isJdkType(clazz) && !isCollection(clazz) && !isArray(clazz))
                || clazz.isEnum();
    }

    public static boolean isJdkType(Class<?> clazz) {
        return clazz.isPrimitive() ||
               clazz.getPackageName().startsWith("java.") ||
               clazz.getPackageName().startsWith("javax.") ||
               clazz.getPackageName().startsWith("jdk.") ||
               clazz.getPackageName().startsWith("com.sun");
    }

    public static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isArray(Class<?> clazz) {
        return clazz.isArray();
    }

}
