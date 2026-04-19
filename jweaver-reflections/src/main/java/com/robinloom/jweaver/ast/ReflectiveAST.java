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
package com.robinloom.jweaver.ast;

import com.robinloom.jweaver.TraversalContext;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.jweaver.annotation.WeaveIgnore;
import com.robinloom.jweaver.annotation.WeaveName;
import com.robinloom.jweaver.ast.nodes.*;
import com.robinloom.jweaver.util.FieldOperations;
import com.robinloom.jweaver.util.SensitivityDetection;
import com.robinloom.jweaver.util.Types;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class ReflectiveAST {

    private final ASTOptions options;
    private final TraversalContext traversalContext;

    public ReflectiveAST() {
        this.options = ASTOptions.defaultOptions();
        this.traversalContext = new TraversalContext(options.getMaxDepth());
    }

    ReflectiveAST(ASTOptions options) {
        this.options = options;
        this.traversalContext = new TraversalContext(options.getMaxDepth());
    }

    public ReflectiveNode build(Object object, WeavingContext ctx) {
        try {
            if (object == null) {
                return new PropertyNode("root", "null");
            }

            traversalContext.reset();
            return toNode(object.getClass().getSimpleName(), object, ctx);

        } catch (Throwable t) {
            return new PropertyNode("root", "[error]");
        }
    }

    private ReflectiveNode toNode(String name, Object value, WeavingContext ctx) {
        if (value == null) {
            return new PropertyNode(name, "null");
        }

        Class<?> type = value.getClass();

        // --- Simple Types ---
        if (Types.isSimpleType(type)) {
            return new PropertyNode(name, ctx.weave(value));
        }

        // --- Collection ---
        if (Types.isCollection(type)) {
            return collection(name, (Collection<?>) value, ctx);
        }

        // --- Array ---
        if (Types.isArray(type)) {
            return array(name, value, ctx);
        }

        // --- Map ---
        if (Types.isMap(type)) {
            return map(name, (Map<?, ?>) value, ctx);
        }

        // --- Map.Entry ---
        if (value instanceof Map.Entry<?, ?> entry) {
            return mapEntry(entry, ctx);
        }

        // --- Complex Object ---
        ReflectiveNode node = (name != null)
                ? new ObjectNode(name, type)
                : new ObjectNode(type);

        return object(node, value, ctx);
    }

    private ReflectiveNode object(ReflectiveNode root, Object object, WeavingContext ctx) {
        if (!traversalContext.enter(object)) {
            return root;
        }

        List<Field> fields = FieldOperations.getFields(object.getClass())
                                            .stream()
                                            .filter(f -> !f.isAnnotationPresent(WeaveIgnore.class))
                                            .toList();

        try {
            for (Field field : fields) {
                try {
                    Object value = readField(field, object);

                    String fieldName;
                    if (field.isAnnotationPresent(WeaveName.class)) {
                        fieldName = field.getAnnotation(WeaveName.class).value();
                    } else {
                        fieldName = field.getName();
                    }

                    if (value == null) {
                        root.addChild(new PropertyNode(fieldName, "null"));
                        continue;
                    }

                    if (SensitivityDetection.isSensitive(field)) {
                        root.addChild(new PropertyNode(fieldName, "***"));
                        continue;
                    }

                    ReflectiveNode child = toNode(fieldName, value, ctx);
                    root.addChild(child);
                } catch (Exception e) {
                    root.addChild(new PropertyNode("[?]", "[?]"));
                }
            }
        } finally {
            traversalContext.exit();
        }

        return root;
    }

    private ReflectiveNode collection(String fieldName, Collection<?> collection, WeavingContext ctx) {
        return sequence(fieldName, collection, collection.size(), collection.getClass(), ctx);
    }

    private ReflectiveNode array(String fieldName, Object array, WeavingContext ctx) {
        int length = Array.getLength(array);

        List<Object> listView = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            listView.add(Array.get(array, i));
        }

        return sequence(fieldName, listView, length, array.getClass(), ctx);
    }

    private ReflectiveNode map(String fieldName, Map<?, ?> map, WeavingContext ctx) {
        return sequence(fieldName, map.entrySet(), map.size(), map.getClass(), ctx);
    }

    private ReflectiveNode mapEntry(Map.Entry<?, ?> entry, WeavingContext ctx) {
        String key;
        Object value = entry.getValue();

        if (Types.isSimpleType(entry.getKey().getClass())) {
            key = ctx.weave(entry.getKey());
        } else {
            key = entry.getKey().toString();
        }

        if (value == null) {
            return new PropertyNode(key, "null");
        }

        if (Types.isSimpleType(value.getClass()) ) {
            return new PropertyNode(key, ctx.weave(value));
        }

        ReflectiveNode node = new MapEntryNode(key);

        sequenceItem(node, entry.getValue(), null, ctx);

        return node;
    }

    private ReflectiveNode sequence(String fieldName, Iterable<?> iterable, int size,
                                    Class<?> displayType, WeavingContext ctx) {

        ReflectiveNode root = new SequenceNode(fieldName, displayType, size);

        if (!traversalContext.enter(iterable)) {
            return root;
        }

        int i = 0;
        for (Object item : iterable) {

            if (i >= options.getMaxSequenceLength()) {
                ReflectiveNode child = new PropertyNode((size - i) + " more");
                child.setIndex(i);

                root.addChild(child);
                break;
            }

            sequenceItem(root, item, i, ctx);
            i++;
        }

        traversalContext.exit();
        return root;
    }

    private void sequenceItem(ReflectiveNode root, Object item, Integer index, WeavingContext ctx) {
        if (item == null) {
            ReflectiveNode node = new PropertyNode("null");
            node.setIndex(index);
            root.addChild(node);
            return;
        }

        String name = null;
        if (Types.isIterableType(item.getClass())) {
            name = item.getClass().getSimpleName();
        }

        ReflectiveNode node = toNode(name, item, ctx);
        if (!(item instanceof Map.Entry)) {
            node.setIndex(index);
        }
        root.addChild(node);
    }

    Object readField(Field field, Object target) throws ReflectiveOperationException {
        field.setAccessible(true);
        return field.get(target);
    }
}
