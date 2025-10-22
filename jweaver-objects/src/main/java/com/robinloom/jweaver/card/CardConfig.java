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
package com.robinloom.jweaver.card;

import com.robinloom.jweaver.commons.WeaverConfig;

public class CardConfig extends WeaverConfig {

    private BoxChars boxChars = BoxChars.UNICODE_LIGHT;

    public CardConfig() {}

    public char getTlBoxChar() {
        return boxChars.tl();
    }

    public char getTrBoxChar() {
        return boxChars.tr();
    }

    public char getBlBoxChar() {
        return boxChars.bl();
    }

    public char getBrBoxChar() {
        return boxChars.br();
    }

    public char getVBoxChar() {
        return boxChars.v();
    }

    public String getHBoxCharNTimes(int n) {
        return String.valueOf(boxChars.h()).repeat(n);
    }

    public void setBoxChars(BoxChars boxChars) {
        this.boxChars = boxChars;
    }

}
