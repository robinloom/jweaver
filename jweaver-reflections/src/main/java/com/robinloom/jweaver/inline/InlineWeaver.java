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
 * {@link Weaver} implementation producing a compact, single-line representation
 * of an object.
 * <p>
 * The {@code InlineWeaver} renders objects in a concise, bracket-based format
 * that emphasizes readability while preserving structural information. It is
 * intended for logging and quick inspection scenarios where space is limited.
 * <p>
 * Objects are represented as:
 * <pre>
 * ClassName[field1=value1, field2=value2]
 * </pre>
 * <p>
 * Nested objects and collections are rendered recursively using the same
 * inline format. Structural traversal is delegated to {@link ReflectiveAST},
 * ensuring consistent handling of cycles and depth limits.
 * <p>
 * This weaver is stateless apart from its internal buffers and is typically
 * instantiated per use.
 */
public class InlineWeaver implements Weaver {
    private final ReflectiveAST ast = new ReflectiveAST();
    private final Loom loom = Loom.empty();

    public InlineWeaver() {}

    /**
     * Produces a compact, single-line representation of the given object.
     *
     * @param object the object to render
     * @param ctx the current weaving context
     * @return a concise, human-readable string representation
     */
    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        ReflectiveNode root = ast.build(object, ctx);

        loom.reset();
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
            loom.append(propertyNode.toString());
            loom.appendIf(!node.isLastChild(), ", ");
        } else if (node instanceof SequenceNode sequenceNode) {
            loom.append(sequenceNode.getFieldName());
            if (!sequenceNode.getFieldName().equals(sequenceNode.getClassName())) {
                loom.eq();
                loom.append(sequenceNode.getClassName());
            }
            loom.lbracket().append(sequenceNode.getSize()).rbracket().space();
            loom.append(opening());
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
