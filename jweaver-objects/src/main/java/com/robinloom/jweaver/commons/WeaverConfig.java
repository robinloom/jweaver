package com.robinloom.jweaver.commons;

import java.lang.reflect.Field;
import java.util.List;

public class WeaverConfig {

    private List<String> includedFields;
    private List<String> excludedFields;

    private int globalLengthLimit;
    private boolean capitalizeFields;
    private boolean showDataTypes;
    private boolean showInheritedFields;
    private int maxDepth;
    private int maxSequenceLength;

    public WeaverConfig() {
        includedFields = Properties.INCLUDED_FIELDS.getStringList(List.of());
        excludedFields = Properties.EXCLUDED_FIELDS.getStringList(List.of());
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
