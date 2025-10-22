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
    
    private final TreeConfig config;
    private final NestedStructureBuilder nestedStructureBuilder;
    private final TreeWeavingMachine machine;

    public TreeWeaver() {
        config = new TreeConfig();
        nestedStructureBuilder = new NestedStructureBuilder(config);
        machine = new TreeWeavingMachine(config);
    }

    /**
     * Sets the maximum depth for the resulting tree.
     * @param maxDepth an integer for the maximum depth
     * @return instance for chaining
     */
    public TreeWeaver maxDepth(int maxDepth) {
        config.setMaxDepth(maxDepth);
        return this;
    }

    /**
     * Sets the maximum length of collections and arrays to be included in the output.
     * Elements that exceed the limit will be summarized (... 57 more).
     * @param maxSequenceLength an integer value
     * @return instance for chaining
     */
    public TreeWeaver maxSequenceLength(int maxSequenceLength) {
        config.setMaxSequenceLength(maxSequenceLength);
        return this;
    }

    /**
     * Sets the character that will be used for a simple branch of the tree.
     * Default is '|'
     * @param branchChar the character to use
     * @return instance for chaining
     */
    public TreeWeaver branchChar(char branchChar) {
        config.setBranchChar(branchChar);
        return this;
    }

    /**
     * Sets the character that will be used as the last char of a branch on a certain level
     * Default is '`'
     * @param lastBranchChar the character to use
     * @return instance for chaining
     */
    public TreeWeaver lastBranchChar(char lastBranchChar) {
        config.setLastBranchChar(lastBranchChar);
        return this;
    }

    /**
     * Sets the names of fields that should be included in the output.
     * By default, every field is included.
     * @param fields list of strings containing included field names
     * @return instance for chaining
     */
    public TreeWeaver includeFields(List<String> fields) {
        config.setIncludedFields(fields);
        config.setExcludedFields(List.of());
        return this;
    }

    /**
     * Sets the names of fields that should be excluded from the output.
     * By default, no field is excluded.
     * @param fields list of strings containing excluded field names
     * @return instance for chaining
     */
    public TreeWeaver excludeFields(List<String> fields) {
        config.setExcludedFields(fields);
        config.setIncludedFields(List.of());
        return this;
    }

    /**
     * Determines if the class name should be omitted when printing.
     * By default, the class name is included.
     * @return instance for chaining
     */
    public TreeWeaver omitClassName() {
        config.setOmitClassName(true);
        return this;
    }

    /**
     * Enables capitalization of field names.
     * firstName -> FirstName
     * @return instance for chaining
     */
    public TreeWeaver capitalizeFields() {
        config.setCapitalizeFields(true);
        return this;
    }

    /**
     * Enables the printing of data types
     * @return instance for chaining
     */
    public TreeWeaver showDataTypes() {
        config.setShowDataTypes(true);
        return this;
    }

    /**
     * Activates the inclusion of inherited fields.
     * @return instance for chaining
     */
    public TreeWeaver showInheritedFields() {
        config.setShowInheritedFields(true);
        return this;
    }

    /**
     * Will order field names alphabetically before printing.
     * @return instance for chaining
     */
    public TreeWeaver orderFieldsAlphabetically() {
        config.setOrderFieldsAlphabetically(true);
        return this;
    }

    /**
     * Generates a string representation of the given object via reflections.
     * Prints the class name followed by every accessible field in a tree structure.
     * For JDK classes, a regular <code>toString()</code> result is returned.
     * Detects reciprocal and circular object dependencies.
     * @param object object to generate a string representation for
     * @return a well-structured, human-readable representation of that object
     */
    public String weave(Object object) {
        if (object == null) {
            return "null";
        }
        if (TypeDictionary.isJdkType(object.getClass())) {
            return object.toString();
        }

        NestedNode tree = nestedStructureBuilder.build(new NestedNode(object), object);

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
            if (config.isIncludeClassName()) {
                machine.append(node.getContent());
            }
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

            machine.append(node.getContent());
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
