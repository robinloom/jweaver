package com.robinloom.jweaver.commons;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class WeaverConfig {

    private List<String> includedFields = new ArrayList<>();
    private List<String> excludedFields = new ArrayList<>();

    private boolean capitalizeFields;
    private boolean showDataTypes;
    private boolean showInheritedFields;
    private int maxSequenceLength = 10;

    public void setIncludedFields(List<String> includedFields) {
        this.includedFields = includedFields;
    }

    public boolean isIncluded(Field field) {
        return !excludedFields.contains(field.getName()) &&
                (includedFields.isEmpty() || includedFields.contains(field.getName()));
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

    public int getMaxSequenceLength() {
        return maxSequenceLength;
    }

    public void setMaxSequenceLength(int maxSequenceLength) {
        this.maxSequenceLength = maxSequenceLength;
    }

}
