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
 * Defines the available output modes for JWeaver.
 * <p>
 * A {@code Mode} represents a concrete, preconfigured output style.
 * Each mode selects an appropriate {@link Weaver} implementation and
 * determines the overall structure and level of detail of the generated
 * string representation.
 * <p>
 * Modes are intentionally explicit and non-combinable. They are designed
 * to be simple, descriptive presets rather than a configurable matrix
 * of options.
 */
public enum Mode {

    /**
     * Compact, single-line representation.
     * <p>
     * Suitable for logging and quick debugging output.
     */
    INLINE,

    /**
     * Hierarchical tree-style representation.
     * <p>
     * Emphasizes object structure and relationships.
     */
    TREE
}