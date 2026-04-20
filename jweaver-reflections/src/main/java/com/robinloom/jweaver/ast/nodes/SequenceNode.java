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
package com.robinloom.jweaver.ast.nodes;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents an ordered collection of elements.
 * <p>
 * A {@code SequenceNode} models arrays, collections, or other iterable
 * structures. Its children correspond to individual elements in iteration order.
 * <p>
 * Each child node may carry an index indicating its position within the sequence.
 * The node also retains the collection type and optional size information.
 */
@NullMarked
public final class SequenceNode extends ReflectiveNode {

    @Nullable private final String fieldName;
    private final Class<?> clazz;
    private final Integer size;

    public SequenceNode(@Nullable String fieldName, Class<?> clazz, Integer size) {
        this.fieldName = fieldName;
        this.clazz = clazz;
        this.size = size;
    }

    @Override
    public String toString() {
        if (fieldName == null || isRoot()) {
            return clazz.getSimpleName() + "[" + size + "]";
        } else {
            return fieldName + "=" + clazz.getSimpleName() + "[" + size + "]";
        }
    }
}
