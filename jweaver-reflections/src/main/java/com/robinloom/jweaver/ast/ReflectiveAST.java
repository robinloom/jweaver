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

    private final int SEQUENCE_LIMIT = 10;
    private final int MAX_DEPTH = 4;
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
        if (depth == MAX_DEPTH) {
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
                    ReflectiveNode child = ReflectiveNode.objectNode(fieldName, value);
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
        ReflectiveNode root = ReflectiveNode.objectNode(fieldName, collection);

        depth++;
        if (depth == MAX_DEPTH) {
            depth--;
            return root;
        }

        int i = 0;
        for (Object item : collection) {
            String prefix = "(" + i + ") ";

            if (i >= SEQUENCE_LIMIT) {
                root.addChild(ReflectiveNode.sequenceItem((collection.size()-i) + " more"));
                break;
            } else if (Types.isSimpleType(item.getClass())) {
                root.addChild(ReflectiveNode.sequenceItem((prefix + ctx.weave(item))));
            } else if (Types.isCollection(item.getClass())) {
                root.addChild(collection(prefix.trim(), (Collection<?>) item, ctx));
            } else if (Types.isArray(item.getClass())) {
                root.addChild(array(prefix.trim(), item, ctx));
            } else {
                ReflectiveNode child = ReflectiveNode.objectNode(prefix + item.getClass().getSimpleName(), item);
                root.addChild(build(child, item, ctx));
            }
            i++;
        }

        depth--;
        return root;
    }

    private ReflectiveNode array(String fieldName, Object array, WeavingContext ctx) {
        ReflectiveNode root = ReflectiveNode.objectNode(fieldName, array);

        depth++;
        if (depth == MAX_DEPTH) {
            depth--;
            return root;
        }

        int arrayLength = Array.getLength(array);
        for (int i = 0; i < arrayLength; i++) {
            Object item = Array.get(array, i);
            String prefix = "[" + i + "] ";

            if (i >= SEQUENCE_LIMIT) {
                root.addChild(ReflectiveNode.sequenceItem((arrayLength - i) + " more"));
                break;
            } else if (Types.isSimpleType(item.getClass())) {
                root.addChild(ReflectiveNode.sequenceItem(prefix + ctx.weave(item)));
            } else if (Types.isCollection(item.getClass())) {
                root.addChild(collection(prefix.trim(), (Collection<?>) item, ctx));
            } else if (Types.isArray(item.getClass())) {
                root.addChild(array(prefix.trim(), item, ctx));
            } else {
                ReflectiveNode child = ReflectiveNode.objectNode(prefix + item.getClass().getSimpleName(), item);
                root.addChild(build(child, item, ctx));
            }
        }
        depth--;
        return root;
    }

    Object readField(Field field, Object target) throws ReflectiveOperationException {
        field.setAccessible(true);
        return field.get(target);
    }
}
