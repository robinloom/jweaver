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
package com.robinloom.jweaver.tree;

import com.robinloom.jweaver.Weaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.jweaver.ast.*;
import com.robinloom.jweaver.ast.nodes.*;
import com.robinloom.loom.Chars;
import com.robinloom.loom.Loom;
import com.robinloom.loom.Symbols;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Weaver} implementation producing a hierarchical, tree-style
 * representation of an object.
 * <p>
 * The {@code TreeWeaver} renders objects as a multi-line structure using
 * indentation and branch markers to visualize the relationships between
 * nested elements. It is designed for debugging and inspection scenarios
 * where understanding object structure is more important than compactness.
 * <p>
 * A typical output resembles:
 * <pre>
 * Person
 * |-- name=Jane
 * |-- age=29
 * `-- address=Address
 *     |-- street=Main St
 *     `-- city=Springfield
 * </pre>
 * <p>
 * Traversal and structure are provided by {@link ReflectiveAST}, ensuring
 * consistent handling of cycles, depth limits, and collections.
 * <p>
 * This weaver is stateful during rendering but intended to be used per
 * invocation.
 */
public class TreeWeaver implements Weaver {

    private final ReflectiveAST ast = new ReflectiveAST();
    private final Loom loom = Loom.empty();

    /**
     * Produces a tree-style representation of the given object.
     *
     * @param object the object to render
     * @param ctx the current weaving context
     * @return a multi-line, structured representation of the object
     */
    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        ReflectiveNode tree = ast.build(object, ctx);

        List<Boolean> siblingsAtCurrentLevel = new ArrayList<>();

        loom.reset();
        traverseDepthFirst(tree, siblingsAtCurrentLevel);
        loom.removeLastNewline();

        return loom.toString();
    }

    private void traverseDepthFirst(ReflectiveNode node, List<Boolean> siblingsAtCurrentLevel) {
        for (int i = 0; i < siblingsAtCurrentLevel.size() - 1; i++) {
            if (siblingsAtCurrentLevel.get(i)) {
                loom.append(Chars.PIPE).spaces(3);
            } else {
                loom.spaces(4);
            }
        }

        if (node.isLastChild()) {
            loom.append(Symbols.LAST_TREE_BRANCH).space();
        } else if (!node.isRoot()) {
            loom.append(Symbols.TREE_BRANCH).space();
        }

        if (node.getIndex() != null) {
            loom.lbracket().append(node.getIndex()).rbracket().space();
        }

        loom.append(node);
        loom.newline();

        List<ReflectiveNode> children = node.getChildren();

        for (int i = 0; i < children.size(); i++) {
            ReflectiveNode child = children.get(i);

            List<Boolean> siblingsAtNextLevel = new ArrayList<>(siblingsAtCurrentLevel);

            siblingsAtNextLevel.add(i < children.size() - 1);
            traverseDepthFirst(child, siblingsAtNextLevel);
        }
    }
}
