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
package com.robinloom.jweaver.flat;

import com.robinloom.jweaver.commons.WeavingMachine;
import com.robinloom.jweaver.util.FieldOperations;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

final class FlatWeavingMachine extends WeavingMachine {

    private final FlatConfig config;

    public FlatWeavingMachine(FlatConfig config) {
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

    void appendFieldName(String field) {
        if (config.isCapitalizeFields()) {
            field = FieldOperations.capitalize(field);
        }
        delegate.append(field);
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

    public void appendInaccessible() {
        delegate.append("[?]");
        newline();
        delegate.append(config.getFieldSeparator());
    }

    public void appendAfterException(Exception e) {
        delegate.append("[ERROR] ");
        delegate.append(e.getClass().getSimpleName());
        newline();
        delegate.append(config.getFieldSeparator());
    }

    public boolean globalLimitReached() {
        return delegate.length() >= config.getGlobalLengthLimit();
    }

    public void appendSuffix() {
        delegate.append(config.getGlobalSuffix());
    }

}
