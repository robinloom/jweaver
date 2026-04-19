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
package com.robinloom.jweaver.ast;

/**
 * Configuration options controlling the traversal and size of the reflective AST.
 * <p>
 * {@code ASTOptions} define limits that prevent excessive traversal of large or
 * cyclic object graphs. They are applied during {@link ReflectiveAST} construction
 * to ensure predictable and bounded output.
 * <p>
 * The following constraints are enforced:
 * <ul>
 *     <li><b>maxDepth</b> – maximum traversal depth before recursion stops</li>
 *     <li><b>maxSequenceLength</b> – maximum number of elements processed in sequences
 *     (collections, arrays, maps)</li>
 * </ul>
 * <p>
 * When limits are reached, traversal is truncated and remaining elements are
 * omitted from the resulting tree.
 * <p>
 * Instances of this class are immutable and can be safely reused.
 */
public final class ASTOptions {

    private final int maxDepth;
    private final int maxSequenceLength;

    public ASTOptions(int maxDepth, int maxSequenceLength) {
        this.maxDepth = maxDepth;
        this.maxSequenceLength = maxSequenceLength;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getMaxSequenceLength() {
        return maxSequenceLength;
    }

    public static ASTOptions defaultOptions() {
        return new ASTOptions(10, 10);
    }
}
