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

import org.jspecify.annotations.NonNull;

/**
 * Defines a strategy for converting an object into a string representation.
 * <p>
 * A {@code Weaver} is responsible for rendering complex objects that are not
 * handled by a specific {@link TypeWeaver}. It typically processes object
 * structures and produces a human-readable representation according to a
 * given {@link Mode}.
 * <p>
 * Different implementations provide different output styles, such as
 * inline, tree, or structured representations.
 * <p>
 * Implementations are expected to be stateless and should not throw exceptions.
 * Nested values should be delegated back to the provided {@link WeavingContext}
 * to ensure consistent processing.
 */
public interface Weaver {

    /**
     * Produces a string representation of the given object.
     *
     * @param object the object to render
     * @param ctx the current weaving context
     * @return a human-readable representation of the object
     */
    String weave(@NonNull Object object, WeavingContext ctx);}
