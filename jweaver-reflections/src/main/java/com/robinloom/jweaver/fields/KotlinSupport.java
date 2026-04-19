package com.robinloom.jweaver.fields;

import java.lang.annotation.Annotation;

final class KotlinSupport {

    private static final String KOTLIN_METADATA = "kotlin.Metadata";

    static boolean isKotlinClass(Class<?> clazz) {
        for (Annotation a : clazz.getDeclaredAnnotations()) {
            if (a.annotationType().getName().equals(KOTLIN_METADATA)) {
                return true;
            }
        }
        return false;
    }
}