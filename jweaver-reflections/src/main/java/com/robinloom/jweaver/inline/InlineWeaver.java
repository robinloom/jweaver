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
import com.robinloom.jweaver.ast.ASTOptions;
import com.robinloom.jweaver.ast.ReflectiveAST;
import com.robinloom.jweaver.ast.ReflectiveNode;
import com.robinloom.jweaver.util.Types;
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

    private final ReflectiveAST ast = new ReflectiveAST(ASTOptions.compact());
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
        if (Types.isJdkType(object.getClass())) {
            return object.toString();
        }

        ReflectiveNode root = ast.build(ReflectiveNode.root(object), object, ctx);
        traverseDepthFirst(root);
        loom.append(closing());

        return loom.toString();
    }

    private void traverseDepthFirst(ReflectiveNode node) {
        if (node.isRoot()) {
            loom.append(node.getClazzName()).append(opening());
        } else if (node.isObject()) {
            loom.append(node.getFieldName());
            loom.eq();
            loom.append(node.getClazzName());
            loom.append(opening());
        } else if (node.isProperty()) {
            loom.append(node.getFieldName());
            loom.eq();
            loom.append(node.getValue());
            loom.appendIf(!node.isLastChild(), ", ");
        } else if (node.isSequenceItem()) {
            loom.append(node.getValue());
            loom.space();
        }

        for (ReflectiveNode child : node.getChildren()) {
            traverseDepthFirst(child);
        }

        if (node.isObject()) {
            loom.append(closing());
        }
    }

    protected String opening() {
        return "[";
    }

    protected String closing() {
        return "]";
    }

}
