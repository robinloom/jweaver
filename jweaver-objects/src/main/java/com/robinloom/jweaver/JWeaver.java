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

import com.robinloom.jweaver.bullet.BulletWeaver;
import com.robinloom.jweaver.card.CardWeaver;
import com.robinloom.jweaver.commons.Weaver;
import com.robinloom.jweaver.flat.FlatWeaver;
import com.robinloom.jweaver.tree.TreeWeaver;

/**
 * The main entry point for object weaving in JWeaver.
 *
 * <p><b>Minimal usage:</b> Most users can simply call {@link #weave(Object)} to get a
 * human-readable, structured string representation of any object without having
 * to write a <code>toString()</code> method or make any configuration decisions:</p>
 *
 * <pre>{@code
 * String result = JWeaver.weave(myObject);
 * System.out.println(result);
 * }</pre>
 *
 * <p>The default weaving strategy automatically handles:
 * <ul>
 *   <li>Small objects: compact one-line output</li>
 *   <li>Large or nested objects: structured tree output</li>
 *   <li>Collections: limited to a safe number of elements</li>
 *   <li>Circular or reciprocal references</li>
 *   <li>Sensitive fields such as passwords or tokens (automatically ignored)</li>
 * </ul>
 * </p>
 *
 * <p><b>Advanced usage:</b> For power users who want specific renderers or a custom
 * context, the {@link Internal} class provides access to different weavers and
 * full control:</p>
 *
 * <pre>{@code
 * String treeResult = JWeaver.Internal.tree().weave(myObject);
 * String bulletResult = JWeaver.Internal.bullet().weave(myObject);
 * }</pre>
 *
 * <p>This design keeps the API minimal and safe for most users while allowing
 * extensibility and experimentation for advanced scenarios.</p>
 */
public final class JWeaver {

    private JWeaver() {}

    /**
     * Generates a string representation of the given object via reflections.
     * Prints the class name followed by every accessible field.
     * For JDK classes, a regular <code>toString()</code> result is returned.
     * Detects reciprocal and circular object dependencies.
     * @param object object to generate a string representation for
     * @return a well-structured, human-readable representation of that object
     */
    public static String weave(Object object) {
        return Internal.DEFAULT_WEAVER.weave(object);
    }

    /**
     * Advanced / internal API.
     * For power users who want specific renderers or custom context.
     * Not recommended for casual use.
     */
    public static final class Internal {

        // Default renderer used by `weave()`
        static final Weaver DEFAULT_WEAVER = new FlatWeaver();

        /** Access FlatWeaver explicitly */
        public static FlatWeaver flat() { return new FlatWeaver(); }

        /** Access TreeWeaver explicitly */
        public static TreeWeaver tree() { return new TreeWeaver(); }

        /** Access BulletWeaver explicitly */
        public static BulletWeaver bullet() { return new BulletWeaver(); }

        /** Access CardWeaver explicitly */
        public static CardWeaver card() { return new CardWeaver(); }

        /** Allows a custom context or renderer */
        public static String weaveWith(Weaver weaver, Object object) {
            return weaver.weave(object);
        }
    }

}
