package com.robinloom.jweaver.dynamic;

import com.robinloom.jweaver.commons.WeavingMachine;
import com.robinloom.jweaver.util.FieldOperations;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

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

    void appendFieldValue(Object value, boolean isLast) {
        delegate.append(value);
        if (!isLast) {
            delegate.append(config.getFieldSeparator());
        }
    }

    void appendArrayFieldValue(Object value) {
        delegate.append("[");
        int length = Array.getLength(value);
        for (int i = 0; i < length; i++) {
            if (i == config.getMaxSequenceLength()) {
                delegate.append("...");
                break;
            }

            delegate.append(Array.get(value, i));
            if (i < length - 1) {
                delegate.append(", ");
            }
        }
        delegate.append("]");
    }

     void appendCollectionFieldValue(Collection<?> value) {
        delegate.append("[");
        Iterator<?> iterator = value.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (i == config.getMaxSequenceLength()) {
                delegate.append("...");
                break;
            }
            delegate.append(iterator.next());
            if (i < value.size() - 1) {
                delegate.append(", ");
            }

            i++;
        }
        delegate.append("]");
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

    public boolean globalLimitReached() {
        return delegate.length() >= config.getGlobalLengthLimit();
    }

    public void appendSuffix() {
        delegate.append(config.getGlobalSuffix());
    }

}
