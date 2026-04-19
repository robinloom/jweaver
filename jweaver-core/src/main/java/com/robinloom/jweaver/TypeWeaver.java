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
 * Specialized {@link Weaver} for handling specific types.
 * <p>
 * A {@code TypeWeaver} provides a custom string representation for a particular
 * target type, overriding the default, structural rendering. It is typically used
 * for well-known types where a concise or domain-specific representation is more
 * useful than a reflective breakdown.
 * <p>
 * This mechanism is especially useful for improving the readability of types
 * with suboptimal or overly verbose {@code toString()} implementations (e.g. many
 * JDK classes), but can be applied to any type.
 * <p>
 * When a {@code TypeWeaver} is available for a given value, it takes precedence
 * over general-purpose {@link Weaver} implementations.
 */
public abstract class TypeWeaver implements Weaver {

    /**
     * Returns the type this weaver is responsible for.
     *
     * @return the target class handled by this weaver
     */
    public abstract Class<?> targetType();
}
