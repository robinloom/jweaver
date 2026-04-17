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
package com.robinloom.jweaver.inline;

import com.robinloom.jweaver.Weaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.jweaver.ast.*;
import com.robinloom.jweaver.ast.nodes.*;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

/**
 * LinearWeaver generates a string representation for a given object by combining
 * the object information (class name, field names, values) with a set of separator strings.
 * The set of separator strings is dynamically configurable.
 * <p>
 * Example:
 * <pre>
 * Person[name=John Doe, birthday=1990-01-01]
 * </pre>
 */
public class InlineWeaver implements Weaver {

    private final ReflectiveAST ast = new ReflectiveAST();
    private final Loom loom = Loom.empty();

    public InlineWeaver() {}

    /**
     * Generates a string representation of the given object via reflections.
     * Prints the class name followed by every accessible field.
     * For JDK classes, a regular <code>toString()</code> result is returned.
     * Detects reciprocal and circular object dependencies.
     * @param object object to generate a string representation for
     * @return a well-structured, human-readable representation of that object
     */
    public String weave(@NonNull Object object, WeavingContext ctx) {
        ReflectiveNode root = ast.build(object, ctx);
        traverseDepthFirst(root);

        return loom.toString();
    }

    private void traverseDepthFirst(ReflectiveNode node) {
        if (node.isRoot()) {
            loom.append(node.getHeader()).append(opening());
        } else if (node instanceof ObjectNode objectNode) {
            loom.when(objectNode.getFieldName() != null, () -> loom.append(objectNode.getFieldName()).eq());
            loom.append(objectNode.getClazz().getSimpleName());
            loom.append(opening());
        } else if (node instanceof PropertyNode propertyNode) {
            loom.append(propertyNode.getFieldName());
            loom.eq();
            loom.append(propertyNode.getValue());
            loom.appendIf(!node.isLastChild(), ", ");
        } else if (node instanceof SequenceNode sequenceNode) {
            loom.append(sequenceNode.getFieldName());
            if (!sequenceNode.getFieldName().equals(sequenceNode.getClassName())) {
                loom.eq();
                loom.append(sequenceNode.getClassName());
            }
            loom.lbracket().append(sequenceNode.getSize()).rbracket().space();
            loom.append(opening());
        } else if (node instanceof SequenceItemNode sequenceItemNode) {
            loom.append(sequenceItemNode.getValue());
            loom.appendIf(!node.isLastChild(), ", ");
        }

        for (ReflectiveNode child : node.getChildren()) {
            traverseDepthFirst(child);
        }

        if (node instanceof ObjectNode || node instanceof SequenceNode || node.isRoot() ) {
            loom.append(closing());
            if (!node.isLastChild() && !(node.isRoot())) {
                loom.commaSpace();
            }
        }
    }

    protected String opening() {
        return "[";
    }

    protected String closing() {
        return "]";
    }

}
