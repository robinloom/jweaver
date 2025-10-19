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

import com.robinloom.jweaver.annotation.WeaveIgnore;

import java.lang.reflect.Field;
import java.util.List;

public class WeaverConfig {

    private List<String> includedFields;
    private List<String> excludedFields;

    private boolean omitClassName;
    private int globalLengthLimit;
    private boolean capitalizeFields;
    private boolean showDataTypes;
    private boolean showInheritedFields;
    private int maxDepth;
    private int maxSequenceLength;

    public WeaverConfig() {
        includedFields = Properties.INCLUDED_FIELDS.getStringList(List.of());
        excludedFields = Properties.EXCLUDED_FIELDS.getStringList(List.of());
        omitClassName = Properties.OMIT_CLASS_NAME.getBool(false);
        globalLengthLimit = Properties.GLOBAL_LENGTH_LIMIT.getInt(10000);
        capitalizeFields = Properties.CAPITALIZE_FIELDS.getBool(false);
        showDataTypes = Properties.SHOW_DATATYPES.getBool(false);
        showInheritedFields = Properties.SHOW_INHERITED_FIELDS.getBool(false);
        maxDepth = Properties.MAX_DEPTH.getInt(4);
        maxSequenceLength = Properties.MAX_SEQUENCE_LENGTH.getInt(10);
    }

    public void setIncludedFields(List<String> includedFields) {
        this.includedFields = includedFields;
    }

    public boolean isIncluded(Field field) {
        return !excludedFields.contains(field.getName()) &&
                (includedFields.isEmpty() || includedFields.contains(field.getName())) &&
                (!field.isAnnotationPresent(WeaveIgnore.class));
    }

    public boolean isIncludeClassName() {
        return !this.omitClassName;
    }

    public void setOmitClassName(boolean omitClassName) {
        this.omitClassName = omitClassName;
    }

    public void setExcludedFields(List<String> excludedFields) {
        this.excludedFields = excludedFields;
    }

    public boolean isCapitalizeFields() {
        return capitalizeFields;
    }

    public void setCapitalizeFields(boolean capitalizeFields) {
        this.capitalizeFields = capitalizeFields;
    }

    public boolean isShowDataTypes() {
        return showDataTypes;
    }

    public void setShowDataTypes(boolean showDataTypes) {
        this.showDataTypes = showDataTypes;
    }

    public boolean isShowInheritedFields() {
        return showInheritedFields;
    }

    public void setShowInheritedFields(boolean showInheritedFields) {
        this.showInheritedFields = showInheritedFields;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getMaxSequenceLength() {
        return maxSequenceLength;
    }

    public void setMaxSequenceLength(int maxSequenceLength) {
        this.maxSequenceLength = maxSequenceLength;
    }

    public int getGlobalLengthLimit() {
        return globalLengthLimit;
    }

    public void setGlobalLengthLimit(int globalLengthLimit) {
        this.globalLengthLimit = globalLengthLimit;
    }
}
