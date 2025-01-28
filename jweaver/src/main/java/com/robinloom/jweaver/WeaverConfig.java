package com.robinloom.jweaver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class WeaverConfig {

    private String classNamePrefix = "";
    private String classNameSuffix = "";
    private String classNameFieldsSeparator = "[";
    private String fieldValueSeparator = "=";
    private String fieldSeparator = ", ";
    private String globalSuffix = "]";

    private List<String> includedFields = new ArrayList<>();
    private List<String> excludedFields = new ArrayList<>();

    private boolean capitalizeFields;
    private boolean showDataTypes;

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

    public void setIncludedFields(List<String> includedFields) {
        this.includedFields = includedFields;
    }

    public boolean isExcluded(Field field) {
        return excludedFields.contains(field.getName()) ||
                (!includedFields.isEmpty() && !includedFields.contains(field.getName()));
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
}
