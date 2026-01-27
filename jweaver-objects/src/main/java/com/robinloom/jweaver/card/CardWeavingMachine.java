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

import com.robinloom.jweaver.commons.WeavingMachine;

import java.util.Map;

final class CardWeavingMachine extends WeavingMachine {

    private int longestField = -1;
    private int overallWidth = -1;

    private final BoxChars boxChars;

    public CardWeavingMachine() {
        this.boxChars = BoxChars.UNICODE_LIGHT;
    }

    void determineLongestField(Map<String, String> wovenFields) {
        for (String key : wovenFields.keySet()) {
            longestField = Math.max(longestField, key.length());
        }
    }

    void determineOverallWidth(Map<String, String> wovenFields, String clazzName) {
        for (Map.Entry<String, String> entry : wovenFields.entrySet()) {
            overallWidth = Math.max(overallWidth, sub(entry).length() + 1);
        }
        overallWidth = Math.max(overallWidth, clazzName.length() + 4);
    }

    void appendClassname(String clazzName) {
        append(boxChars.tl());
        space();
        append(clazzName);
        space();
        append(boxChars.h(overallWidth-clazzName.length()-3));

        append(boxChars.tr());
    }

    void appendField(Map.Entry<String, String> field) {
        String sub = sub(field);
        append(sub);
        append(" ".repeat(overallWidth-sub.length()));
        append(boxChars.v());
    }

    String sub(Map.Entry<String, String> field) {
        StringBuilder sub = new StringBuilder();
        sub.append(boxChars.v());
        sub.append(" ");
        sub.append(field.getKey());
        sub.append(" ".repeat(longestField-field.getKey().length()+1));
        sub.append(": ");
        sub.append(field.getValue());
        return sub.toString();
    }

    void appendFooter() {
        newline();
        append(boxChars.bl());
        append(boxChars.h(overallWidth-1));
        append(boxChars.br());
    }

}
