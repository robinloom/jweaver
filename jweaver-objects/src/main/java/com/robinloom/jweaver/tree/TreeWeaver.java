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

import com.robinloom.jweaver.Mode;
import com.robinloom.jweaver.commons.Weaver;
import com.robinloom.jweaver.structure.NestedNode;
import com.robinloom.jweaver.structure.NestedStructureBuilder;
import com.robinloom.jweaver.util.TypeDictionary;

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
    
    private final TreeWeavingMachine machine = new TreeWeavingMachine();

    public String weave(Object object) {
        return weave(object, Mode.TREE);
    }

    /**
     * Generates a string representation of the given object via reflections.
     * Prints the class name followed by every accessible field in a tree structure.
     * For JDK classes, a regular <code>toString()</code> result is returned.
     * Detects reciprocal and circular object dependencies.
     * @param object object to generate a string representation for
     * @return a well-structured, human-readable representation of that object
     */
    public String weave(Object object, Mode mode) {
        if (object == null) {
            return "null";
        }
        if (TypeDictionary.isJdkType(object.getClass())) {
            return object.toString();
        }

        NestedNode tree = new NestedStructureBuilder().build(new NestedNode(object), object);

        List<Boolean> siblingsAtCurrentLevel = new ArrayList<>();
        traverseDepthFirst(tree, siblingsAtCurrentLevel);
        machine.removeLastNewline();

        String result = machine.toString();
        machine.reset();
        return result;
    }

    private void traverseDepthFirst(NestedNode node, List<Boolean> siblingsAtCurrentLevel) {
        if (machine.globalLimitReached()) {
            return;
        }
        if (node.isRoot()) {
           machine.appendln(node.getContent());
        } else {
            for (int i = 0; i < siblingsAtCurrentLevel.size() - 1; i++) {
                if (siblingsAtCurrentLevel.get(i)) {
                    machine.indentWithCrossingBranch();
                } else {
                    machine.indent();
                }
            }

            if (node.isLastChild()) {
                machine.appendLastBranch();
            } else {
                machine.appendBranch();
            }

            machine.appendln(node.getContent());
        }

        List<NestedNode> children = node.getChildren();

        for (int i = 0; i < children.size(); i++) {
            NestedNode child = children.get(i);

            List<Boolean> siblingsAtNextLevel = new ArrayList<>(siblingsAtCurrentLevel);

            siblingsAtNextLevel.add(i < children.size() - 1);
            traverseDepthFirst(child, siblingsAtNextLevel);
        }
    }
}
