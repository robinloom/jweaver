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
import com.robinloom.jweaver.ast.ReflectiveAST;
import com.robinloom.jweaver.ast.ReflectiveNode;
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
            loom.append(node.getClassName()).newline();
        } else {
            for (int i = 0; i < node.getLevel(); i++) {
                loom.indent();
            }

            loom.append("- ");
            if (node.getIndex() != null) {
                loom.lbracket().append(node.getIndex()).rbracket().space();
            }

            if (node.isObject()) {
                loom.when(node.getFieldName() != null, () -> loom.append(node.getFieldName()).eq());
                loom.append(node.getClassName());
            } else if (node.isProperty()) {
                loom.append(node.getFieldName());
                loom.eq();
                loom.append(node.getValue());
            } else if (node.isSequence()) {
                loom.append(node.getFieldName());
                if (!node.getFieldName().equals(node.getClassName())) {
                    loom.eq();
                    loom.append(node.getClassName());
                }
                loom.lbracket().append(node.getSize()).rbracket();
            } else if (node.isSequenceItem()) {
                loom.append(node.getValue());
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
