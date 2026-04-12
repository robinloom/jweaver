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
package com.robinloom.jweaver.card;

import com.robinloom.jweaver.Weaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.jweaver.ast.ASTOptions;
import com.robinloom.jweaver.ast.ReflectiveAST;
import com.robinloom.jweaver.ast.ReflectiveNode;
import com.robinloom.jweaver.util.Types;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.util.*;

/**
 * CardWeaver depicts a given object as a framed card
 * with aligned key-value pairs inside a box.
 * This representation focuses on clarity and aesthetics,
 * making it well-suited for logs, debug output, or summaries.
 * <p>
 * Example:
 * <pre>
 * ╭ User ───────────────╮
 * │ name  : Alice       │
 * │ age   : 28          │
 * │ email : alice@a.com │
 * ╰─────────────────────╯
 * </pre>
 */
public class CardWeaver implements Weaver {

    private final ReflectiveAST ast = new ReflectiveAST(ASTOptions.expanded());

    private final BoxChars boxChars = BoxChars.UNICODE_LIGHT;

    /**
     * Generates a string representation of the given object via reflections.
     * Prints the class name followed by every accessible field in an ordered structure
     * resembling a business card.
     * For JDK classes, a regular <code>toString()</code> result is returned.
     * Detects reciprocal and circular object dependencies.
     * @param object object to generate a string representation for
     * @return a well-structured, human-readable representation of that object
     */
    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        if (Types.isJdkType(object.getClass())) {
            return object.toString();
        }

        ReflectiveNode root = ast.build(ReflectiveNode.root(object), object, ctx);

        LinkedHashMap<String, String> wovenFields = new LinkedHashMap<>();

        for (ReflectiveNode child : root.getChildren()) {
            String key = child.getFieldName();
            String value = mapValue(child);

            if (key != null) {
                wovenFields.put(key, value);
            }
        }

        String clazzName = root.getClazzName();

        int longestField = determineLongestField(wovenFields);
        int overallWidth = determineOverallWidth(wovenFields, clazzName, longestField);

        Loom loom = Loom.empty();

        // Top border
        loom.append(boxChars.tl())
                .space()
                .append(clazzName)
                .space()
                .append(boxChars.h(overallWidth - clazzName.length() - 3))
                .append(boxChars.tr());

        // Body
        for (Map.Entry<String, String> entry : wovenFields.entrySet()) {
            loom.newline();

            String sub = sub(entry, longestField);
            loom.append(sub);
            loom.spaces(overallWidth - sub.length());
            loom.append(boxChars.v());
        }

        // Bottom border
        loom.newline()
                .append(boxChars.bl())
                .append(boxChars.h(overallWidth - 1))
                .append(boxChars.br());

        return loom.toString();
    }

    /**
     * Maps a node to a displayable string value for CARD mode.
     */
    private String mapValue(ReflectiveNode node) {
        if (node.isProperty()) {
            return node.getValue();
        }

        String type = node.getClazzName();
        int size = node.getChildren().size();

        return type + "(" + size + ")";
    }

    private int determineLongestField(Map<String, String> wovenFields) {
        int longest = -1;
        for (String key : wovenFields.keySet()) {
            longest = Math.max(longest, key.length());
        }
        return longest;
    }

    private int determineOverallWidth(Map<String, String> wovenFields, String clazzName, int longestField) {
        int overallWidth = -1;
        for (Map.Entry<String, String> entry : wovenFields.entrySet()) {
            overallWidth = Math.max(overallWidth, sub(entry, longestField).length() + 1);
        }
        overallWidth = Math.max(overallWidth, clazzName.length() + 4);
        return overallWidth;
    }

    private String sub(Map.Entry<String, String> field, int longestField) {
        Loom loom = Loom.empty();
        loom.append(BoxChars.UNICODE_LIGHT.v());
        loom.space();
        loom.append(field.getKey());
        loom.spaces(longestField - field.getKey().length() + 1);
        loom.colonSpace();
        loom.append(field.getValue());
        return loom.toString();
    }
}
