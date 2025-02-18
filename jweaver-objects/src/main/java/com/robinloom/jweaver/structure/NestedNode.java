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

public class NestedNode {

    private String fieldName;
    private NestedNode parent;
    private final String value;
    private final List<NestedNode> children = new ArrayList<>();

    public NestedNode(Object object) {
        this.value = object.getClass().getSimpleName();
    }

    NestedNode(String value) {
        this.value = value;
    }

    NestedNode(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    void addChild(String value) {
        addChild(null, value);
    }

    void addChild(String fieldName, String value) {
        NestedNode child = new NestedNode(fieldName, value);
        child.parent = this;
        children.add(child);
    }

    void addChild(NestedNode child) {
        child.parent = this;
        children.add(child);
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLastChild() {
        if (isRoot()) {
            return false;
        } else {
            return parent.getChildren().getLast().equals(this);
        }
    }

    public String getContent() {
        if (fieldName == null) {
            return value;
        } else {
            return fieldName + "=" + value;
        }
    }

    public List<NestedNode> getChildren() {
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
