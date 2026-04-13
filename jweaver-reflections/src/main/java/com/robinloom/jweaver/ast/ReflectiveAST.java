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

import com.robinloom.jweaver.WeavingContext;
import com.robinloom.jweaver.annotation.WeaveIgnore;
import com.robinloom.jweaver.annotation.WeaveName;
import com.robinloom.jweaver.util.FieldOperations;
import com.robinloom.jweaver.util.SensitivityDetection;
import com.robinloom.jweaver.util.Types;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class ReflectiveAST {

    protected static final ThreadLocal<Set<Object>> history
            = ThreadLocal.withInitial(() -> Collections.newSetFromMap(new IdentityHashMap<>()));

    private final ASTOptions options;

    private int depth = 0;

    public ReflectiveAST() {
        this.options = ASTOptions.defaultOptions();
    }

    public ReflectiveNode build(Object object, WeavingContext ctx) {

        if (object instanceof Collection<?> col) {
            return collection(object.getClass().getSimpleName(), col, ctx);
        }

        if (object instanceof Map<?, ?> map) {
            return map(object.getClass().getSimpleName(), map, ctx);
        }

        if (object.getClass().isArray()) {
            return array(Array.class.getSimpleName(), object, ctx);
        }

        ReflectiveNode root = ReflectiveNode.root(object);
        return object(root, object, ctx);
    }

    private ReflectiveNode object(ReflectiveNode root, Object object, WeavingContext ctx) {
        if (history.get().contains(object)) {
            return root;
        } else {
            history.get().add(object);
        }

        depth++;
        if (depth == options.getMaxDepth()) {
            depth--;
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
                    root.addChild(ReflectiveNode.property(fieldName, "null"));
                    continue;
                }

                if (SensitivityDetection.isSensitive(field)) {
                    root.addChild(ReflectiveNode.property(fieldName, "***"));
                    continue;
                }

                if (Types.isSimpleType(field.getType())) {
                    root.addChild(ReflectiveNode.property(fieldName, ctx.weave(value)));
                } else if (Types.isCollection(field.getType())) {
                    root.addChild(collection(fieldName, (Collection<?>) value, ctx));
                } else if (Types.isArray(field.getType())) {
                    root.addChild(array(fieldName, value, ctx));
                } else {
                    ReflectiveNode child = ReflectiveNode.objectNode(fieldName, value.getClass());
                    root.addChild(object(child, value, ctx));
                }
            } catch (Exception e) {
                root.addChild(ReflectiveNode.sequenceItem("[?]", -1));
            }
        }

        history.get().clear();

        depth--;
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

    private ReflectiveNode sequence(String fieldName, Iterable<?> iterable, int size,
                                    Class<?> displayType, WeavingContext ctx) {

        ReflectiveNode root = ReflectiveNode.sequence(fieldName, displayType, size);

        depth++;
        if (depth == options.getMaxDepth()) {
            depth--;
            return root;
        }

        int i = 0;
        for (Object item : iterable) {

            if (i >= options.getMaxSequenceLength()) {
                root.addChild(ReflectiveNode.sequenceItem((size - i) + " more", i));
                break;
            }

            handleSequenceItem(root, item, i, ctx);
            i++;
        }

        depth--;
        return root;
    }

    private void handleSequenceItem(ReflectiveNode root, Object item, int index, WeavingContext ctx) {
        if (item == null) {
            root.addChild(ReflectiveNode.sequenceItem("null", index));
            return;
        }

        if (Types.isSimpleType(item.getClass())) {
            root.addChild(ReflectiveNode.sequenceItem(ctx.weave(item), index));
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
        } else {
            ReflectiveNode child = ReflectiveNode.objectNode(item.getClass());

            child.setIndex(index);
            root.addChild(object(child, item, ctx));
        }
    }

    Object readField(Field field, Object target) throws ReflectiveOperationException {
        field.setAccessible(true);
        return field.get(target);
    }
}
