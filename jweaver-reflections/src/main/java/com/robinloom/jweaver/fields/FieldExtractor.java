/*
 * Copyright (C) 2025 Robin Kösters
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
package com.robinloom.jweaver.fields;

import com.robinloom.jweaver.annotation.WeaveIgnore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class FieldExtractor {

    private final Map<Class<?>, List<Field>> CACHE = new ConcurrentHashMap<>();

    public List<Field> extract(Class<?> clazz) {
        return CACHE.computeIfAbsent(clazz, this::compute);
    }

    private List<Field> compute(Class<?> clazz) {
        boolean isKotlin = KotlinSupport.isKotlinClass(clazz);
        return collectFields(clazz).stream()
                                   .filter(f -> isRelevant(f, isKotlin) && isIncluded(f))
                                   .toList();
    }

    private List<Field> collectFields(Class<?> clazz) {
        return List.of(clazz.getDeclaredFields());
    }

    private boolean isRelevant(Field field, boolean isKotlin) {
        if (Modifier.isStatic(field.getModifiers())) {
            return false;
        }
        if (field.isSynthetic()) {
            return false;
        }
        if (isKotlin) {
            if (field.getName().contains("$")) {
                return false;
            }
            return !field.getName().equals("Companion");
        }

        return true;
    }

    private boolean isIncluded(Field field) {
        return !field.isAnnotationPresent(WeaveIgnore.class);
    }

}
