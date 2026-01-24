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

import com.robinloom.jweaver.Mode;
import com.robinloom.jweaver.commons.Weaver;
import com.robinloom.jweaver.structure.NestedNode;
import com.robinloom.jweaver.structure.NestedStructureBuilder;
import com.robinloom.jweaver.util.TypeDictionary;

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

    private final BulletWeavingMachine machine = new BulletWeavingMachine();

    public String weave(Object object) {
        return weave(object, Mode.BULLET);
    }

    /**
     * Generates a string representation of the given object via reflections.
     * Prints the class name followed by every accessible field in a list structure.
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

        NestedNode structure = new NestedStructureBuilder().build(new NestedNode(object), object);
        traverseDepthFirst(structure);

        machine.removeLastNewline();

        String result = machine.toString();
        machine.reset();
        return result;
    }

    private void traverseDepthFirst(NestedNode node) {
        if (machine.globalLimitReached()) {
            return;
        }
        if (node.isRoot()) {
            machine.append(node.getContent());

        } else {
            machine.indent(node.getLevel());
            machine.append(node.getContent());
        }

        for (NestedNode child : node.getChildren()) {
            traverseDepthFirst(child);
        }
    }

}
