/*
 * Copyright (C) 2026 Robin Kösters
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
package com.robinloom.jweaver.ast.nodes;

import com.robinloom.jweaver.ast.ReflectiveAST;

import java.util.ArrayList;
import java.util.List;

/**
 * Base type for all nodes in the reflective object tree.
 * <p>
 * A {@code ReflectiveNode} represents a single element in the structure
 * produced by {@link ReflectiveAST}. Nodes form a hierarchical, acyclic tree
 * that models the logical composition of an object rather than its raw
 * runtime representation.
 * <p>
 * Each node may have:
 * <ul>
 *     <li>a name or key identifying it within its parent</li>
 *     <li>an optional index (for sequence elements)</li>
 *     <li>zero or more child nodes</li>
 * </ul>
 * <p>
 * Concrete subclasses define the semantic role of a node, such as object,
 * property, sequence, or map entry.
 */
public sealed abstract class ReflectiveNode permits ObjectNode, PropertyNode, SequenceNode, MapEntryNode {

    protected ReflectiveNode parent;
    protected List<ReflectiveNode> children = new ArrayList<>();
    protected Integer index;

    public void addChild(ReflectiveNode child) {
        child.parent = this;
        children.add(child);
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public List<ReflectiveNode> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLastChild() {
        return parent != null && parent.getChildren().getLast().equals(this);
    }

}
