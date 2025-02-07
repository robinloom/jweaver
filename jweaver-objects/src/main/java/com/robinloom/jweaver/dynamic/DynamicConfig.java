package com.robinloom.jweaver.dynamic;

import com.robinloom.jweaver.commons.WeaverConfig;

class DynamicConfig extends WeaverConfig {

    private String classNamePrefix = "";
    private String classNameSuffix = "";
    private String classNameFieldsSeparator = "[";
    private String fieldValueSeparator = "=";
    private String fieldSeparator = ", ";
    private String globalSuffix = "]";

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
