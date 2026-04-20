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

/**
 * Represents a single entry within a map structure.
 * <p>
 * A {@code MapEntryNode} models a key-value pair. The key is represented as the
 * node's name, while the value is represented by its child node.
 * <p>
 * Complex values are expanded into child nodes, while simple values may be
 * represented directly as {@link PropertyNode}s.
 */
public final class MapEntryNode extends ReflectiveNode {

    private final String key;

    public MapEntryNode(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
