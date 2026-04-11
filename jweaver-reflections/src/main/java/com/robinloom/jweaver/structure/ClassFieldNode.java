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
package com.robinloom.jweaver.structure;

import java.util.ArrayList;
import java.util.List;

public class ClassFieldNode {

    private Class<?> clazz;

    private String fieldName;
    private String value;

    private ClassFieldNode parent;
    private final List<ClassFieldNode> children = new ArrayList<>();

    public static ClassFieldNode root(Object object) {
        return new ClassFieldNode(object);
    }

    public static ClassFieldNode leaf(String fieldName, String value) {
        return new ClassFieldNode(fieldName, value);
    }

    public static ClassFieldNode innerNode(String value) {
        return new ClassFieldNode(value);
    }

    private ClassFieldNode(String value) {
        this.value = value;
    }

    private ClassFieldNode(Object object) {
        this.clazz = object.getClass();
    }

    private ClassFieldNode(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    void addChild(String value) {
        addChild(null, value);
    }

    void addChild(String fieldName, String value) {
        ClassFieldNode child = new ClassFieldNode(fieldName, value);
        child.parent = this;
        children.add(child);
    }

    void addChild(ClassFieldNode child) {
        child.parent = this;
        children.add(child);
    }

    public boolean isRoot() {
        return parent == null;
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

    public List<ClassFieldNode> getChildren() {
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
