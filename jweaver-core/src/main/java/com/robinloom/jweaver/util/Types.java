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
package com.robinloom.jweaver.util;

import java.util.Collection;
import java.util.Map;

public final class Types {

    private Types() {}

    public static boolean isSimpleType(Class<?> clazz) {
        return (isJdkType(clazz) && !isCollection(clazz) && !isArray(clazz) && !isMap(clazz) &&
                !Map.Entry.class.isAssignableFrom(clazz)) ||  clazz.isEnum();
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

    public static boolean isMap(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz) ;
    }

}
