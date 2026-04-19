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
 * TreeWeaver depicts a given object and its fields as a tree.
 * This representation is particularly suitable for nested objects.
 * After setting up the tree structure, it is traversed in a depth-first search.
 * The object string is built while traversing the tree.
 * <p>
 * Example:
 * <pre>
 * Person
 * |-- name=John Doe
 * `-- birthday=1990-01-01
 * </pre>
 */
public class TreeWeaver implements Weaver {

    private final ReflectiveAST ast = new ReflectiveAST();
    private final Loom loom = Loom.empty();

    /**
     * Generates a string representation of the given object via reflections.
     * Prints the class name followed by every accessible field in a tree structure.
     * For JDK classes, a regular <code>toString()</code> result is returned.
     * Detects reciprocal and circular object dependencies.
     * @param object object to generate a string representation for
     * @return a well-structured, human-readable representation of that object
     */
    public String weave(@NonNull Object object, WeavingContext ctx) {
        ReflectiveNode tree = ast.build(object, ctx);

        List<Boolean> siblingsAtCurrentLevel = new ArrayList<>();

        loom.reset();
        traverseDepthFirst(tree, siblingsAtCurrentLevel);
        loom.removeLastNewline();

        return loom.toString();
    }

    private void traverseDepthFirst(ReflectiveNode node, List<Boolean> siblingsAtCurrentLevel) {
        if (node.isRoot()) {
           loom.line(node.getHeader());
        } else {
            for (int i = 0; i < siblingsAtCurrentLevel.size() - 1; i++) {
                if (siblingsAtCurrentLevel.get(i)) {
                    loom.append(Chars.PIPE).spaces(3);
                } else {
                    loom.spaces(4);
                }
            }

            if (node.isLastChild()) {
                loom.append(Symbols.LAST_TREE_BRANCH).space();
            } else {
                loom.append(Symbols.TREE_BRANCH).space();
            }

            if (node.getIndex() != null) {
                loom.lbracket().append(node.getIndex()).rbracket().space();
            }

            switch (node) {
                case ObjectNode objectNode -> {
                    loom.when(objectNode.getFieldName() != null, () -> loom.append(objectNode.getFieldName()).eq());
                    loom.append(objectNode.getClazz().getSimpleName());
                }
                case PropertyNode propertyNode -> loom.append(propertyNode.toString());
                case SequenceNode sequenceNode -> {
                    loom.append(sequenceNode.getFieldName());
                    if (!sequenceNode.getFieldName().equals(sequenceNode.getClassName())) {
                        loom.eq();
                        loom.append(sequenceNode.getClassName());
                    }
                    if (sequenceNode.getSize() != null) {
                        loom.lbracket().append(sequenceNode.getSize()).rbracket();
                    }
                }
                case MapEntryNode mapEntryNode -> loom.append(mapEntryNode.getKey());
            }

            loom.newline();
        }

        List<ReflectiveNode> children = node.getChildren();

        for (int i = 0; i < children.size(); i++) {
            ReflectiveNode child = children.get(i);

            List<Boolean> siblingsAtNextLevel = new ArrayList<>(siblingsAtCurrentLevel);

            siblingsAtNextLevel.add(i < children.size() - 1);
            traverseDepthFirst(child, siblingsAtNextLevel);
        }
    }
}
