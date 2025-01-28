package com.robinloom.jweaver;

import java.lang.reflect.Field;

public final class WeavingMachine {

    private final StringBuilder delegate;
    public WeaverConfig config;

    public WeavingMachine(WeaverConfig config) {
        this.config = config;
        delegate = new StringBuilder();
    }

    public WeavingMachine append(String string) {
        delegate.append(string);
        return this;
    }

    public void appendClassName(String className) {
        delegate.append(config.getClassNamePrefix());
        delegate.append(className);
        delegate.append(config.getClassNameSuffix());
        delegate.append(config.getClassNameFieldsSeparator());
    }

    public void appendDataType(Field field) {
        if (config.isShowDataTypes()) {
            delegate.append(field.getType().getSimpleName());
            delegate.append(" ");
        }
    }

    public void appendFieldName(Field field) {
        String fieldName = field.getName();
        if (config.isCapitalizeFields()) {
            fieldName = capitalize(fieldName);
        }
        delegate.append(fieldName);
        delegate.append(config.getFieldValueSeparator());
    }

    public void appendFieldValue(Object value) {
        delegate.append(value);
        delegate.append(config.getFieldSeparator());
    }

    public void appendInaccessible() {
        delegate.append("[?]");
        delegate.append(config.getFieldSeparator());
    }

    public void appendAfterException(Exception e) {
        delegate.append("[ERROR] ");
        delegate.append(e.getClass().getSimpleName());
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

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public String toString() {
        return delegate.toString();
    }

}
