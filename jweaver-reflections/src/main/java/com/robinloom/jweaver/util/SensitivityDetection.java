/*
 * Copyright (C) 2026 Robin Kösters
 * mail[at]robinloom[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
