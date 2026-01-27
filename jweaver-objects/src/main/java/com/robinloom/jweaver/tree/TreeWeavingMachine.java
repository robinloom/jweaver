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

    void appendln(String string) {
        append(string);
        newline();
    }

    void indent() {
        append(" ".repeat(4));
    }

    void indentWithCrossingBranch() {
        append("|");
        append(" ".repeat(3));
    }

    void appendBranch() {
        append("|");
        append("--");
        space();
    }

    void appendLastBranch() {
        append("`");
        append("--");
        space();
    }

    public boolean globalLimitReached() {
        return delegate.length() >= 10_000;
    }

}