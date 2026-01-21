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
package com.robinloom.jweaver.linear;

import com.robinloom.jweaver.commons.Properties;
import com.robinloom.jweaver.commons.WeaverConfig;

class LinearConfig extends WeaverConfig {

    private String classNamePrefix;
    private String classNameSuffix;
    private String classNameFieldsSeparator;
    private String fieldValueSeparator;
    private String fieldSeparator;
    private String globalSuffix;

    public LinearConfig() {
        classNamePrefix = Properties.DYNAMIC_CLASS_NAME_PREFIX.get("");
        classNameSuffix = Properties.DYNAMIC_CLASS_NAME_SUFFIX.get("");
        classNameFieldsSeparator = Properties.DYNAMIC_CLASSNAME_FIELDS_SEPARATOR.get("[");
        fieldValueSeparator = Properties.DYNAMIC_FIELD_VALUE_SEPARATOR.get("=");
        fieldSeparator = Properties.DYNAMIC_FIELD_SEPARATOR.get(", ");
        globalSuffix = Properties.DYNAMIC_GLOBAL_SUFFIX.get("]");
    }

    public String getClassNamePrefix() {
        return classNamePrefix;
    }

    public void setClassNamePrefix(String classNamePrefix) {
        this.classNamePrefix = classNamePrefix;
    }

    public String getClassNameSuffix() {
        return classNameSuffix;
    }

    public void setClassNameSuffix(String classNameSuffix) {
        this.classNameSuffix = classNameSuffix;
    }

    public String getClassNameFieldsSeparator() {
        return classNameFieldsSeparator;
    }

    public void setClassNameFieldsSeparator(String classNameFieldsSeparator) {
        this.classNameFieldsSeparator = classNameFieldsSeparator;
    }

    public String getFieldValueSeparator() {
        return fieldValueSeparator;
    }

    public void setFieldValueSeparator(String fieldValueSeparator) {
        this.fieldValueSeparator = fieldValueSeparator;
    }

    public String getFieldSeparator() {
        return fieldSeparator;
    }

    public void setFieldSeparator(String fieldSeparator) {
        this.fieldSeparator = fieldSeparator;
    }

    public String getGlobalSuffix() {
        return globalSuffix;
    }

    public void setGlobalSuffix(String globalSuffix) {
        this.globalSuffix = globalSuffix;
    }
}
