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
        traversalContext.reset();

        if (object instanceof Collection<?> col) {
            return collection(object.getClass().getSimpleName(), col, ctx);
        }

        if (object instanceof Map<?, ?> map) {
            return map(object.getClass().getSimpleName(), map, ctx);
        }

        if (object.getClass().isArray()) {
            return array(Array.class.getSimpleName(), object, ctx);
        }

        ReflectiveNode root = new ObjectNode(object.getClass());
        return object(root, object, ctx);
    }

    private ReflectiveNode object(ReflectiveNode root, Object object, WeavingContext ctx) {
        if (!traversalContext.enter(object)) {
            return root;
        }

        List<Field> fields = FieldOperations.getFields(object.getClass())
                                            .stream()
                                            .filter(f -> !f.isAnnotationPresent(WeaveIgnore.class))
                                            .toList();

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

                if (Types.isSimpleType(field.getType())) {
                    root.addChild(new PropertyNode(fieldName, ctx.weave(value)));
                } else if (Types.isCollection(field.getType())) {
                    root.addChild(collection(fieldName, (Collection<?>) value, ctx));
                } else if (Types.isArray(field.getType())) {
                    root.addChild(array(fieldName, value, ctx));
                } else if (Types.isMap(field.getType())) {
                    root.addChild(map(fieldName, (Map<?, ?>) value, ctx));
                } else {
                    ReflectiveNode child = new ObjectNode(fieldName, value.getClass());
                    root.addChild(object(child, value, ctx));
                }
            } catch (Exception e) {
                root.addChild(new SequenceItemNode("[?]", -1));
            }
        }

        traversalContext.exit();
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

        if (Types.isSimpleType(value.getClass()) ) {
            return new PropertyNode(key, ctx.weave(value));
        }

        ReflectiveNode node = new MapEntryNode(key);

        handleSequenceItem(node, entry.getValue(), null, ctx);

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
                root.addChild(new SequenceItemNode((size - i) + " more", i));
                break;
            }

            handleSequenceItem(root, item, i, ctx);
            i++;
        }

        traversalContext.exit();
        return root;
    }

    private void handleSequenceItem(ReflectiveNode root, Object item, Integer index, WeavingContext ctx) {
        if (item == null) {
            root.addChild(new SequenceItemNode("null", index));
            return;
        }

        if (Types.isSimpleType(item.getClass())) {
            root.addChild(new SequenceItemNode(ctx.weave(item), index));
        } else if (Types.isCollection(item.getClass())) {
            ReflectiveNode child = sequence(item.getClass().getSimpleName(),
                                            (Collection<?>) item,
                                            ((Collection<?>) item).size(),
                                            item.getClass(),
                                            ctx);

            child.setIndex(index);
            root.addChild(child);
        } else if (Types.isArray(item.getClass())) {
            ReflectiveNode child = array(item.getClass().getSimpleName(), item, ctx);

            child.setIndex(index);
            root.addChild(child);
        } else if (Types.isMap(item.getClass())) {
            ReflectiveNode child = map(item.getClass().getSimpleName(), (Map<?, ?>) item, ctx);

            child.setIndex(index);
            root.addChild(child);
        } else if (item instanceof Map.Entry<?,?> entry) {
            ReflectiveNode child = mapEntry(entry, ctx);

            root.addChild(child);
        } else {
            ReflectiveNode child = new ObjectNode(item.getClass());

            child.setIndex(index);
            root.addChild(object(child, item, ctx));
        }
    }

    Object readField(Field field, Object target) throws ReflectiveOperationException {
        field.setAccessible(true);
        return field.get(target);
    }
}
