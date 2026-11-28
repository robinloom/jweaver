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

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Maintains state during reflective traversal of an object graph.
 * <p>
 * The {@code TraversalContext} ensures that traversal remains finite and
 * produces an acyclic tree structure. It prevents both excessive recursion
 * depth and repeated processing of the same object instance.
 * <p>
 * Three constraints are enforced:
 * <ul>
 *     <li><b>Maximum depth</b> – traversal stops once the configured depth is reached</li>
 *     <li><b>Maximum nodes</b> – traversal stops after {@value #DEFAULT_MAX_NODES} entered
 *     objects, which bounds shared sub-graphs that are expanded once per reference</li>
 *     <li><b>Visited tracking</b> – object identity is tracked to avoid revisiting
 *     the same instance and creating cycles</li>
 * </ul>
 * <p>
 * Object identity (not equality) is used to detect previously visited elements.
 * <p>
 * Usage follows a strict enter/exit pattern:
 * <pre>
 * if (context.enter(obj)) {
 *     try {
 *         // process object
 *     } finally {
 *         context.exit();
 *     }
 * }
 * </pre>
 * <p>
 * This class is stateful and intended for use within a single traversal.
 * It must be {@link #reset() reset} before reuse.
 */
public final class TraversalContext {

    private final Set<Object> visited = Collections.newSetFromMap(new IdentityHashMap<>());

    static final int DEFAULT_MAX_NODES = 1000;

    private final int maxDepth;
    private final int maxNodes;
    private int depth = 0;
    private int nodes = 0;

    public TraversalContext(int maxDepth) {
        this(maxDepth, DEFAULT_MAX_NODES);
    }

    TraversalContext(int maxDepth, int maxNodes) {
        this.maxDepth = maxDepth;
        this.maxNodes = maxNodes;
    }

    /**
     * Attempts to enter the given object for traversal.
     *
     * @param o the object to enter
     * @return {@code true} if traversal may proceed, {@code false} if the object
     *         was already visited or a depth or node limit has been reached
     */
    public boolean enter(Object o) {
        if (isLimitReached()) {
            return false;
        }

        if (!visited.add(o)) {
            return false;
        }

        depth++;
        nodes++;
        return true;
    }

    /**
     * Returns whether the depth or node limit has been reached.
     */
    public boolean isLimitReached() {
        return depth >= maxDepth || nodes >= maxNodes;
    }

    /**
     * Signals that traversal of the current object has completed.
     * <p>
     * Must be paired with a successful {@link #enter(Object)} call.
     */
    public void exit(Object o) {
        depth--;
        visited.remove(o);
    }

    /**
     * Resets the traversal state.
     * <p>
     * Clears all visited objects and resets depth and node count, allowing the
     * context to be reused for a new traversal.
     */
    public void reset() {
        visited.clear();
        depth = 0;
        nodes = 0;
    }
}
