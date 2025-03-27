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
package com.robinloom.jweaver.bullet;

import com.robinloom.jweaver.commons.Weaver;
import com.robinloom.jweaver.structure.NestedNode;
import com.robinloom.jweaver.structure.NestedStructureBuilder;
import com.robinloom.jweaver.util.TypeDictionary;

import java.util.List;

/**
 * BulletWeaver generates a string representation for a given object by arranging
 * the object information (class name, field names, values) in a list with indented items.
 * <p>
 * Example:
 * <pre>
 * Person
 *  - name=John Doe
 *  - birthday=1990-01-01
 * </pre>
 */
public class BulletWeaver implements Weaver {

    private final BulletConfig config;
    private final NestedStructureBuilder nestedStructureBuilder;
    private final BulletWeavingMachine machine;

    public BulletWeaver() {
        config = new BulletConfig();
        nestedStructureBuilder = new NestedStructureBuilder(config);
        machine = new BulletWeavingMachine(config);
    }

    /**
     * Sets the character to be used for a bullet on first level.
     * @param character character to be used
     * @return instance for chaining
     */
    public BulletWeaver firstLevelBulletChar(char character) {
        config.setFirstLevelBulletChar(character);
        return this;
    }

    /**
     * Sets the character to be used for a bullet on second level.
     * @param character character to be used
     * @return instance for chaining
     */
    public BulletWeaver secondLevelBulletChar(char character) {
        config.setSecondLevelBulletChar(character);
        return this;
    }

    /**
     * Sets the character to be used for a bullet on third level or deeper.
     * @param character character to be used
     * @return instance for chaining
     */
    public BulletWeaver deeperLevelBulletChar(char character) {
        config.setDeeperLevelBulletChar(character);
        return this;
    }

    /**
     * Sets the amount of indentation to be applied for every step.
     * Default is 2.
     * @param indentation integer for the indentation level
     * @return instance for chaining
     */
    public BulletWeaver indentation(int indentation) {
        config. setIndentation(indentation);
        return this;
    }

    /**
     * Sets the maximum depth for the resulting tree.
     * @param maxDepth an integer for the maximum depth
     * @return instance for chaining
     */
    public BulletWeaver maxDepth(int maxDepth) {
        config.setMaxDepth(maxDepth);
        return this;
    }

    /**
     * Sets the maximum length of collections and arrays to be included in the output.
     * Elements that exceed the limit will be summarized (... 57 more).
     * @param maxSequenceLength an integer value
     * @return instance for chaining
     */
    public BulletWeaver maxSequenceLength(int maxSequenceLength) {
        config.setMaxSequenceLength(maxSequenceLength);
        return this;
    }

    /**
     * Sets the names of fields that should be included in the output.
     * By default, every field is included.
     * @param fields list of strings containing included field names
     * @return instance for chaining
     */
    public BulletWeaver includeFields(List<String> fields) {
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
    public BulletWeaver excludeFields(List<String> fields) {
        config.setExcludedFields(fields);
        config.setIncludedFields(List.of());
        return this;
    }

    /**
     * Determines if the class name should be omitted when printing.
     * By default, the class name is included.
     * @return instance for chaining
     */
    public BulletWeaver omitClassName() {
        config.setOmitClassName(true);
        return this;
    }

    /**
     * Enables capitalization of field names.
     * firstName -> FirstName
     * @return instance for chaining
     */
    public BulletWeaver capitalizeFields() {
        config.setCapitalizeFields(true);
        return this;
    }

    /**
     * Enables the printing of data types
     * @return instance for chaining
     */
    public BulletWeaver showDataTypes() {
        config.setShowDataTypes(true);
        return this;
    }

    /**
     * Activates the inclusion of inherited fields.
     * @return instance for chaining
     */
    public BulletWeaver showInheritedFields() {
        config.setShowInheritedFields(true);
        return this;
    }

    /**
     * Will order field names alphabetically before printing.
     * @return instance for chaining
     */
    public BulletWeaver orderFieldsAlphabetically() {
        config.setOrderFieldsAlphabetically(true);
        return this;
    }

    /**
     * Generates a string representation of the given object via reflections.
     * Prints the class name followed by every accessible field in a list structure.
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

        NestedNode structure = nestedStructureBuilder.build(new NestedNode(object), object);
        traverseDepthFirst(structure);

        machine.removeLastNewline();
        return machine.toString();
    }

    private void traverseDepthFirst(NestedNode node) {
        if (machine.globalLimitReached()) {
            return;
        }
        if (node.isRoot()) {
            if (config.isIncludeClassName()) {
                machine.append(node.getContent());
            }
        } else {
            machine.indent(node.getLevel());
            machine.append(node.getContent());
        }

        for (NestedNode child : node.getChildren()) {
            traverseDepthFirst(child);
        }
    }

}
