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
 * Represents a complex object in the reflective tree.
 * <p>
 * An {@code ObjectNode} corresponds to a non-simple value whose internal
 * structure is expanded via reflection. Its children represent the object's
 * fields or properties.
 * <p>
 * The node retains the object's type and an optional field name if it is
 * nested within another object.
 */
@NullMarked
public final class ObjectNode extends ReflectiveNode {

    @Nullable private final String fieldName;
    private final Class<?> clazz;

    public ObjectNode(Class<?> clazz) {
        this.clazz = clazz;
        this.fieldName = null;
    }

    public ObjectNode(String fieldName, Class<?> clazz) {
        this.fieldName = fieldName;
        this.clazz = clazz;
    }

    public @Nullable String getFieldName() {
        return fieldName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public String getHeader() {
        return getClazz().getSimpleName();
    }
}
