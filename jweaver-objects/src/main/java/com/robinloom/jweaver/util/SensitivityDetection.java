package com.robinloom.jweaver.util;

import com.robinloom.jweaver.annotation.WeaveRedact;

import java.lang.reflect.Field;
import java.util.Set;

public final class SensitivityDetection {

    private SensitivityDetection() {}

    public static boolean isSensitive(Field field) {
        return isSensitiveByAnnotation(field) || isSensitiveByType(field) || isSensitiveByName(field);
    }

    private static boolean isSensitiveByAnnotation(Field field) {
        return field.isAnnotationPresent(WeaveRedact.class);
    }

    private static boolean isSensitiveByType(Field field) {
        Class<?> type = field.getType();
        return type == char[].class || type == byte[].class;
    }

    private static boolean isSensitiveByName(Field field) {
        return Set.of("password", "passwd", "pwd", "secret", "token",
                      "apiKey", "apikey", "privateKey", "privatekey")
                .contains(field.getName());
    }
}
