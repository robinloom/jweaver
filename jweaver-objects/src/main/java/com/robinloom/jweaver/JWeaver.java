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
package com.robinloom.jweaver;

import com.robinloom.jweaver.commons.Weaver;

/**
 * Central entry point of the JWeaver API.
 * <p>
 * JWeaver provides a simple, zero-decision way to generate human-readable
 * string representations of arbitrary Java objects using reflection.
 * It is designed as a drop-in replacement for handwritten {@code toString()}
 * implementations.
 * <p>
 * The default {@link #weave(Object)} method uses the {@link Mode#INLINE} mode,
 * which produces a compact, single-line representation suitable for logging
 * and debugging. Alternative output styles can be selected explicitly via
 * {@link #weave(Object, Mode)}.
 * <p>
 * Internally, JWeaver selects an appropriate {@code Weaver} implementation
 * based on the chosen {@link Mode}. Circular object references are detected
 * automatically to ensure safe output.
 * <p>
 * This class is intentionally non-instantiable and exposes only static
 * convenience methods.
 *
 * @see Mode
 */
public final class JWeaver {

    private JWeaver() {}

    /**
     * Generates a string representation of the given object using the default
     * {@link Mode#INLINE} mode.
     *
     * @param object the object to generate a string representation for
     * @return a human-readable string representation of the given object
     */
    public static String weave(Object object) {
        return weave(object, Mode.INLINE);
    }

    /**
     * Generates a string representation of the given object using the specified
     * {@link Mode}.
     *
     * @param object the object to generate a string representation for
     * @param mode the output mode controlling structure and level of detail
     * @return a human-readable string representation of the given object
     */
    public static String weave(Object object, Mode mode) {
        Weaver weaver = Mode.getWeaverForMode(mode);
        return weaver.weave(object, mode);
    }

}