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

import com.robinloom.jweaver.TypeWeaver;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a simple value in the reflective tree.
 * <p>
 * A {@code PropertyNode} is a leaf node containing a scalar or already
 * stringified value, such as primitives, strings, or values handled by
 * {@link TypeWeaver}s.
 * <p>
 * Property nodes do not have children.
 */
@NullMarked
public final class PropertyNode extends ReflectiveNode {

    @Nullable private final String fieldName;
    private final String value;

    public PropertyNode(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    public PropertyNode(String value) {
        this.fieldName = null;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (fieldName == null) {
            return value;
        } else {
            return fieldName + "=" + value;
        }
    }
}
