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
package com.robinloom.jweaver;

/**
 * Resolves a {@link TypeWeaver} for a given type.
 * <p>
 * A {@code TypeWeaverResolver} determines whether a specific type has a
 * dedicated {@link TypeWeaver} that should be used instead of the default
 * reflective rendering.
 * <p>
 * If a matching {@code TypeWeaver} is found, it takes precedence over
 * general-purpose {@link Weaver} implementations.
 * <p>
 * Implementations typically maintain a registry of known {@link TypeWeaver}s
 * and select the most suitable one based on the given class.
 */
public interface TypeWeaverResolver {

    /**
     * Returns a {@link TypeWeaver} for the given class, if available.
     *
     * @param clazz the type to resolve
     * @return a matching {@code TypeWeaver}, or {@code null} if none is applicable
     */
    TypeWeaver resolve(Class<?> clazz);
}
