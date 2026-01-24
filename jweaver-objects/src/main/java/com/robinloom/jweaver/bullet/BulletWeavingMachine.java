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
package com.robinloom.jweaver.bullet;

import com.robinloom.jweaver.commons.WeavingMachine;

final class BulletWeavingMachine extends WeavingMachine {

    void indent(int level) {
        delegate.append(" ".repeat(2 * level - 1));
        if (level == 1) {
            hyphen();
            space();
        } else if (level == 2) {
            hyphen();
            space();
        } else if (level > 2) {
            hyphen();
            space();
        }
    }

    void append(String string) {
        delegate.append(string);
        newline();
    }

    boolean globalLimitReached() {
        return delegate.length() >= 10_000;
    }
}
