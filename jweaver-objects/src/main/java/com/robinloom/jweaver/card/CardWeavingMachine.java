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

    private final CardConfig config;

    public CardWeavingMachine(CardConfig config) {
        this.config = config;
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
        if (config.isIncludeClassName()) {
            overallWidth = Math.max(overallWidth, clazzName.length() + 4);
        }
    }

    void appendClassname(String clazzName) {
        delegate.append(config.getTlBoxChar());
        if (config.isIncludeClassName()) {
            space();
            delegate.append(clazzName);
            space();
            delegate.append(config.getHBoxCharNTimes(overallWidth-clazzName.length()-3));
        } else {
            delegate.append(config.getHBoxCharNTimes(overallWidth-1));
        }
        delegate.append(config.getTrBoxChar());
    }

    void appendField(Map.Entry<String, String> field) {
        String sub = sub(field);
        delegate.append(sub);
        delegate.append(" ".repeat(overallWidth-sub.length()));
        delegate.append(config.getVBoxChar());
    }

    String sub(Map.Entry<String, String> field) {
        StringBuilder sub = new StringBuilder();
        sub.append(config.getVBoxChar());
        sub.append(" ");
        sub.append(field.getKey());
        sub.append(" ".repeat(longestField-field.getKey().length()+1));
        sub.append(": ");
        sub.append(field.getValue());
        return sub.toString();
    }

    void appendFooter() {
        newline();
        delegate.append(config.getBlBoxChar());
        delegate.append(config.getHBoxCharNTimes(overallWidth-1));
        delegate.append(config.getBrBoxChar());
    }

}
