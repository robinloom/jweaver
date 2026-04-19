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
 * Context object coordinating the weaving process.
 * <p>
 * The {@code WeavingContext} acts as the central dispatcher for converting
 * arbitrary objects into their string representation. It determines how a
 * value is processed by resolving either a {@link TypeWeaver} (for specific
 * types) or a general {@link Weaver} based on the selected {@link Mode}.
 * <p>
 * The context is propagated recursively during traversal. Each nested call
 * receives a derived child context, allowing the weaving process to distinguish
 * between the root object and nested values.
 * <p>
 * Resolution order:
 * <ul>
 *     <li>If a {@link TypeWeaver} is available for the value's type, it is used.</li>
 *     <li>Otherwise, a mode-specific {@link Weaver} is used for reflective processing.</li>
 * </ul>
 * <p>
 * Instances of this class are immutable and lightweight. A new child context
 * is created for each nested weaving step.
 */
public final class WeavingContext {

    private final Mode mode;
    private final TypeWeaverResolver typeWeaverResolver;
    private final ReflectionWeaverResolver reflectionWeaverResolver;
    private final boolean isRoot;

    public WeavingContext(Mode mode,
                          TypeWeaverResolver typeWeaverResolver,
                          ReflectionWeaverResolver reflectionWeaverResolver,
                          boolean isRoot) {
        this.mode = mode;
        this.typeWeaverResolver = typeWeaverResolver;
        this.reflectionWeaverResolver = reflectionWeaverResolver;
        this.isRoot = isRoot;
    }

    /**
     * Converts the given value into its string representation.
     *
     * @param value the value to weave (may be {@code null})
     * @return a human-readable string representation
     */
    public String weave(Object value) {
        if (value == null) {
            return "null";
        }

        TypeWeaver typeWeaver = typeWeaverResolver.resolve(value.getClass());
        if (typeWeaver != null) {
            return typeWeaver.weave(value, childContext());
        }

        Weaver weaver = reflectionWeaverResolver.resolve(mode);
        return weaver.weave(value, childContext());
    }

    /**
     * Indicates whether the current weaving operation is at the root level.
     *
     * @return {@code true} if this context represents the root object,
     *         {@code false} for nested values
     */
    public boolean isRoot() {
        return isRoot;
    }

    private WeavingContext childContext() {
        return new WeavingContext(mode, typeWeaverResolver, reflectionWeaverResolver, false);
    }
}
