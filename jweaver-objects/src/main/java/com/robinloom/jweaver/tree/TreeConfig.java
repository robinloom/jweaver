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

import com.robinloom.jweaver.commons.Properties;
import com.robinloom.jweaver.commons.WeaverConfig;

class TreeConfig extends WeaverConfig {

    private char branchChar;
    private char lastBranchChar;

    public TreeConfig() {
        branchChar = Properties.TREE_BRANCH_CHAR.getChar('|');
        lastBranchChar = Properties.TREE_BRANCH_CHAR.getChar('`');
    }

    public char getBranchChar() {
        return branchChar;
    }

    public void setBranchChar(char branchChar) {
        this.branchChar = branchChar;
    }

    public char getLastBranchChar() {
        return lastBranchChar;
    }

    public void setLastBranchChar(char lastBranchChar) {
        this.lastBranchChar = lastBranchChar;
    }
}
