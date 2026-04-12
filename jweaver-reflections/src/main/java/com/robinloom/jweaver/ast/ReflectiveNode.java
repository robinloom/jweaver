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

import java.util.ArrayList;
import java.util.List;

public class ReflectiveNode {

    public enum Type {
        ROOT,
        OBJECT,
        PROPERTY,
        SEQUENCE
    }

    private final Type type;

    private final Class<?> clazz;
    private final String fieldName;
    private final String value;

    private ReflectiveNode parent;
    private final List<ReflectiveNode> children = new ArrayList<>();

    private ReflectiveNode(Type type, String fieldName, Class<?> clazz, String value) {
        this.type = type;
        this.fieldName = fieldName;
        this.clazz = clazz;
        this.value = value;
    }

    public static ReflectiveNode root(Object object) {
        return new ReflectiveNode(Type.ROOT, null, object.getClass(), null);
    }

    public static ReflectiveNode objectNode(String fieldName, Class<?> clazz) {
        return new ReflectiveNode(Type.OBJECT, fieldName, clazz, null);
    }

    public static ReflectiveNode property(String fieldName, String value) {
        return new ReflectiveNode(Type.PROPERTY, fieldName, null, value);
    }

    public static ReflectiveNode sequenceItem(String value) {
        return new ReflectiveNode(Type.SEQUENCE, null, null, value);
    }

    public void addChild(ReflectiveNode child) {
        child.parent = this;
        children.add(child);
    }

    public boolean isRoot() {
        return type == Type.ROOT;
    }

    public boolean isObject() {
        return type == Type.OBJECT;
    }

    public boolean isProperty() {
        return type == Type.PROPERTY;
    }

    public boolean isSequenceItem() {
        return type == Type.SEQUENCE;
    }

    public boolean isLastChild() {
        return parent.getChildren().getLast().equals(this);
    }

    public String getClazzName() {
        return clazz.getSimpleName();
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getValue() {
        return value;
    }

    public List<ReflectiveNode> getChildren() {
        return children;
    }

    public int getLevel() {
        if (isRoot()) {
            return 0;
        } else {
            return parent.getLevel() + 1;
        }
    }
}
