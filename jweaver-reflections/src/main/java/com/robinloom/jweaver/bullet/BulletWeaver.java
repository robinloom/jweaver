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

import com.robinloom.jweaver.Weaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.jweaver.ast.*;
import com.robinloom.jweaver.ast.nodes.*;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

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

    private final ReflectiveAST ast = new ReflectiveAST();

    /**
     * Generates a string representation of the given object via reflections.
     * Prints the class name followed by every accessible field in a list structure.
     * For JDK classes, a regular <code>toString()</code> result is returned.
     * Detects reciprocal and circular object dependencies.
     * @param object object to generate a string representation for
     * @return a well-structured, human-readable representation of that object
     */
    public String weave(@NonNull Object object, WeavingContext ctx) {
        ReflectiveNode structure = ast.build(object, ctx);

        Loom loom = Loom.empty();
        traverseDepthFirst(structure, loom);
        loom.removeLastNewline();

        return loom.toString();
    }

    private void traverseDepthFirst(ReflectiveNode node, Loom loom) {
        if (node.isRoot()) {
            loom.append(node.getHeader()).newline();
        } else {
            for (int i = 0; i < node.getLevel(); i++) {
                loom.indent();
            }

            loom.append("- ");
            if (node.getIndex() != null) {
                loom.lbracket().append(node.getIndex()).rbracket().space();
            }

            switch (node) {
                case ObjectNode objectNode -> {
                    loom.when(objectNode.getFieldName() != null, () -> loom.append(objectNode.getFieldName()).eq());
                    loom.append(objectNode.getClazz().getSimpleName());
                }
                case PropertyNode propertyNode -> {
                    loom.append(propertyNode.getFieldName());
                    loom.eq();
                    loom.append(propertyNode.getValue());
                }
                case SequenceNode sequenceNode -> {
                    loom.append(sequenceNode.getFieldName());
                    if (!sequenceNode.getFieldName().equals(sequenceNode.getClazz().getSimpleName())) {
                        loom.eq();
                        loom.append(sequenceNode.getClazz().getSimpleName());
                    }
                    loom.lbracket().append(sequenceNode.getSize()).rbracket();
                }
                case SequenceItemNode sequenceItemNode -> loom.append(sequenceItemNode.getValue());
                default -> {
                }
            }

            loom.newline();

            for (int i = 0; i < node.getLevel(); i++) {
                loom.outdent();
            }
        }

        for (ReflectiveNode child : node.getChildren()) {
            traverseDepthFirst(child, loom);
        }
    }

}
