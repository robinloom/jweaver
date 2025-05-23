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
package com.robinloom.jweaver.tree;

import com.robinloom.jweaver.commons.WeavingMachine;

final class TreeWeavingMachine extends WeavingMachine {

    private final TreeConfig config;

    TreeWeavingMachine(TreeConfig config) {
        this.config = config;
    }

    void append(String string) {
        delegate.append(string);
        newline();
    }

    void indent() {
        delegate.append(" ".repeat(4));
    }

    void indentWithCrossingBranch() {
        delegate.append("|");
        delegate.append(" ".repeat(3));
    }

    void appendBranch() {
        delegate.append(config.getBranchChar());
        delegate.append("-- ");
    }

    void appendLastBranch() {
        delegate.append(config.getLastBranchChar());
        delegate.append("-- ");
    }

    public boolean globalLimitReached() {
        return delegate.length() >= config.getGlobalLengthLimit();
    }

}