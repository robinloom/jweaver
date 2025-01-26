package com.robinloom.jweaver;

public class WeaverConfig {

    private String classNamePrefix = "";
    private String classNameSuffix = "[";
    private String fieldValueSeparator = "=";
    private String fieldSeparator = ", ";
    private String globalSuffix = "]";

    private boolean capitalizeFields;

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

    public boolean isCapitalizeFields() {
        return capitalizeFields;
    }

    public void setCapitalizeFields(boolean capitalizeFields) {
        this.capitalizeFields = capitalizeFields;
    }

}
