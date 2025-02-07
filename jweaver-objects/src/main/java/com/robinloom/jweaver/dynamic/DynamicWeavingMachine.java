package com.robinloom.jweaver.dynamic;

import com.robinloom.jweaver.commons.WeavingMachine;
import com.robinloom.jweaver.util.FieldOperations;

import java.lang.reflect.Field;

final class DynamicWeavingMachine extends WeavingMachine {

    private final DynamicConfig config;

    public DynamicWeavingMachine(DynamicConfig config) {
        this.config = config;
    }

    void appendClassName(String className) {
        delegate.append(config.getClassNamePrefix());
        delegate.append(className);
        delegate.append(config.getClassNameSuffix());
        delegate.append(config.getClassNameFieldsSeparator());
    }

    void appendDataType(Field field) {
        if (config.isShowDataTypes()) {
            delegate.append(field.getType().getSimpleName());
            delegate.append(" ");
        }
    }

    void appendFieldName(Field field) {
        String fieldName = field.getName();
        if (config.isCapitalizeFields()) {
            fieldName = FieldOperations.capitalize(fieldName);
        }
        delegate.append(fieldName);
        delegate.append(config.getFieldValueSeparator());
    }

    void appendFieldValue(Object value) {
        delegate.append(value);
        delegate.append(config.getFieldSeparator());
    }

    @Override
    public void appendInaccessible() {
        super.appendInaccessible();
        delegate.append(config.getFieldSeparator());
    }

    @Override
    public void appendAfterException(Exception e) {
        super.appendAfterException(e);
        delegate.append(config.getFieldSeparator());
    }

    public void appendSuffix() {
        deleteLast();
        delegate.append(config.getGlobalSuffix());
    }

    private void deleteLast() {
        String fieldSeparator = config.getFieldSeparator();
        if (!fieldSeparator.isEmpty()) {
            delegate.delete(delegate.length() - fieldSeparator.length(), delegate.length());
        }
    }

}
