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
import java.util.function.Function;

public class ReflectiveAST {

    protected static final ThreadLocal<Set<Object>> history
            = ThreadLocal.withInitial(() -> Collections.newSetFromMap(new IdentityHashMap<>()));

    private final ASTOptions options;

    private int depth = 0;

    public ReflectiveAST(ASTOptions options) {
        this.options = options;
    }

    public ReflectiveNode build(ReflectiveNode root, Object object, WeavingContext ctx) {
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

                    if (options.isExpandSequences()) {
                        root.addChild(collection(fieldName, (Collection<?>) value, ctx));
                    }  else {
                        root.addChild(ReflectiveNode.property(fieldName, ctx.weave(value)));
                    }

                } else if (Types.isArray(field.getType())) {

                    if (options.isExpandSequences()) {
                        root.addChild(array(fieldName, value, ctx));
                    }  else {
                        root.addChild(ReflectiveNode.property(fieldName, ctx.weave(value)));
                    }

                } else {
                    ReflectiveNode child = ReflectiveNode.objectNode(fieldName, value.getClass());
                    root.addChild(build(child, value, ctx));
                }
            } catch (Exception e) {
                root.addChild(ReflectiveNode.sequenceItem("[?]"));
            }
        }

        history.get().clear();

        depth--;
        return root;
    }

    private ReflectiveNode collection(String fieldName, Collection<?> collection, WeavingContext ctx) {
        return sequence(fieldName, collection, collection.size(), collection.getClass(), i -> "(" + i + ") ", ctx);
    }

    private ReflectiveNode array(String fieldName, Object array, WeavingContext ctx) {
        int length = Array.getLength(array);

        List<Object> listView = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            listView.add(Array.get(array, i));
        }

        return sequence(fieldName, listView, length, array.getClass(), i -> "[" + i + "] ", ctx);
    }

    private ReflectiveNode sequence(String fieldName, Iterable<?> iterable, int size, Class<?> displayType,
                                    Function<Integer, String> indexFormatter, WeavingContext ctx) {

        ReflectiveNode root = ReflectiveNode.objectNode(fieldName, displayType);
        depth++;
        if (depth == options.getMaxDepth()) {
            depth--;
            return root;
        }

        int i = 0;
        for (Object item : iterable) {
            String prefix = indexFormatter.apply(i);

            if (i >= options.getMaxSequenceLength()) {
                root.addChild(ReflectiveNode.sequenceItem((size - i) + " more"));
                break;
            }

            handleSequenceItem(root, item, prefix, ctx);
            i++;
        }

        depth--;
        return root;
    }

    private void handleSequenceItem(ReflectiveNode root, Object item, String prefix, WeavingContext ctx) {
        if (item == null) {
            root.addChild(ReflectiveNode.sequenceItem(prefix + "null"));
            return;
        }

        if (Types.isSimpleType(item.getClass())) {
            root.addChild(ReflectiveNode.sequenceItem(prefix + ctx.weave(item)));
        } else if (Types.isCollection(item.getClass())) {
            root.addChild(sequence(prefix.trim(), (Collection<?>) item, ((Collection<?>) item).size(),
                                   item.getClass(), i -> "(" + i + ") ", ctx));
        } else if (Types.isArray(item.getClass())) {
            root.addChild(array(prefix.trim(), item, ctx));
        } else {
            ReflectiveNode child = ReflectiveNode.objectNode(prefix + item.getClass().getSimpleName(), item.getClass());
            root.addChild(build(child, item, ctx));
        }
    }

    Object readField(Field field, Object target) throws ReflectiveOperationException {
        field.setAccessible(true);
        return field.get(target);
    }
}
