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
package com.robinloom.jweaver.commons;

import java.util.Arrays;
import java.util.List;

public enum Properties {
    INCLUDED_FIELDS("com.robinloom.jweaver.includedFields"),
    EXCLUDED_FIELDS("com.robinloom.jweaver.excludedFields"),
    GLOBAL_LENGTH_LIMIT("com.robinloom.jweaver.globalLengthLimit"),
    CAPITALIZE_FIELDS("com.robinloom.jweaver.capitalizeFields"),
    SHOW_DATATYPES("com.robinloom.jweaver.showDataTypes"),
    SHOW_INHERITED_FIELDS("com.robinloom.jweaver.showInheritedFields"),
    MAX_DEPTH("com.robinloom.jweaver.maxDepth"),
    MAX_SEQUENCE_LENGTH("com.robinloom.jweaver.maxSequenceLength"),
    BULLET_FIRST_LEVEL_CHAR("com.robinloom.jweaver.bullet.firstLevelChar"),
    BULLET_SECOND_LEVEL_CHAR("com.robinloom.jweaver.bullet.secondLevelChar"),
    BULLET_DEEPER_LEVEL_CHAR("com.robinloom.jweaver.bullet.deeperLevelChar"),
    BULLET_INDENTATION("com.robinloom.jweaver.bullet.indentation"),
    DYNAMIC_CLASS_NAME_PREFIX("com.robinloom.jweaver.dynamic.classNamePrefix"),
    DYNAMIC_CLASS_NAME_SUFFIX("com.robinloom.jweaver.dynamic.classNameSuffix"),
    DYNAMIC_CLASSNAME_FIELDS_SEPARATOR("com.robinloom.jweaver.dynamic.classnameFieldsSeparator"),
    DYNAMIC_FIELD_VALUE_SEPARATOR("com.robinloom.jweaver.dynamic.fieldValueSeparator"),
    DYNAMIC_FIELD_SEPARATOR("com.robinloom.jweaver.dynamic.fieldSeparator"),
    DYNAMIC_GLOBAL_SUFFIX("com.robinloom.jweaver.dynamic.globalSuffix"),
    TREE_BRANCH_CHAR("com.robinloom.jweaver.tree.branchChar"),
    TREE_LAST_BRANCH_CHAR("com.robinloom.jweaver.tree.lastBranchChar");

    private final String propertyKey;

    Properties(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public int getInt(int defaultValue) {
        try {
            return Integer.parseInt(System.getProperty(propertyKey));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public char getChar(char defaultValue) {
        String value = System.getProperty(propertyKey);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        } else {
            return value.charAt(0);
        }
    }

    public boolean getBool(boolean defaultValue) {
        String value = System.getProperty(propertyKey);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        } else {
            return Boolean.parseBoolean(value);
        }
    }

    public List<String> getStringList(List<String> defaultValue) {
        String value = System.getProperty(propertyKey);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        } else {
            return Arrays.stream(value.split(",")).toList();
        }
    }

    public String get(String defaultValue) {
        String value = System.getProperty(propertyKey);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        } else {
            return value;
        }
    }
}
