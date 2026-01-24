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

import com.robinloom.jweaver.bullet.BulletWeaver;
import com.robinloom.jweaver.card.CardWeaver;
import com.robinloom.jweaver.commons.Weaver;
import com.robinloom.jweaver.linear.LinearWeaver;
import com.robinloom.jweaver.tree.TreeWeaver;

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
     * Flat, multi-line representation with improved readability.
     */
    MULTILINE,

    /**
     * Multi-line representation with increased level of detail.
     * <p>
     * Typically includes deeper object traversal or additional metadata.
     */
    MULTILINE_VERBOSE,

    /**
     * Hierarchical tree-style representation.
     * <p>
     * Emphasizes object structure and relationships.
     */
    TREE,

    /**
     * Bullet-point style representation.
     * <p>
     * Focuses on concise field listing.
     */
    BULLET,

    /**
     * Card-style representation.
     * <p>
     * Presents objects in a structured, sectioned layout.
     */
    CARD;

    /**
     * Indicates whether this mode produces multi-line output.
     *
     * @return {@code true} if the mode uses a multi-line layout,
     *         {@code false} otherwise
     */
    public boolean isMultiline() {
        return this == MULTILINE || this == MULTILINE_VERBOSE;
    }

    /**
     * Returns a new {@link Weaver} instance suitable for the given mode.
     * <p>
     * The returned weaver is stateless and may be instantiated per call.
     *
     * @param mode the mode to resolve a weaver for
     * @return a corresponding {@link Weaver} implementation
     */
    public static Weaver getWeaverForMode(Mode mode) {
        return switch (mode) {
            case TREE -> new TreeWeaver();
            case BULLET -> new BulletWeaver();
            case CARD -> new CardWeaver();
            case null, default -> new LinearWeaver();
        };
    }
}