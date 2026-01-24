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

import com.robinloom.jweaver.Mode;
import com.robinloom.jweaver.commons.WeavingMachine;
import com.robinloom.jweaver.util.FieldOperations;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

final class LinearWeavingMachine extends WeavingMachine {

    private final int SEQUENCE_LIMIT = 10;
    private final Mode mode;

    public LinearWeavingMachine(Mode mode) {
        this.mode = mode;
    }

    void appendClassName(String className) {
        if (mode == Mode.INLINE) {
            append(className);
            lbracket();
        } else if (mode.isMultiline()) {
            equals();
            space();
            append(className);
            space();
            equals();
        }
    }

    void appendDataType(Field field) {
        if (mode == Mode.MULTILINE_VERBOSE) {
            append(field.getType().getSimpleName());
            space();
        }
    }

    void appendFieldName(String field) {
        if (mode == Mode.MULTILINE_VERBOSE) {
            field = FieldOperations.capitalize(field);
        }
        append(field);
        if (mode == Mode.INLINE) {
            equals();
        } else if (mode.isMultiline()) {
            colon();
            space();
        }
    }

    void appendFieldValue(Object value, boolean isLast) {
        append(value);
        if (!isLast) {
            if (mode == Mode.INLINE) {
                comma();
                space();
            } else if (mode.isMultiline()) {
                newline();
            }
        }
    }

    void appendArrayFieldValue(Object value) {
        lbracket();
        int length = Array.getLength(value);
        for (int i = 0; i < length; i++) {
            if (i == SEQUENCE_LIMIT) {
                append("...");
                break;
            }

            append(Array.get(value, i));
            if (i < length - 1) {
                comma();
                space();
            }
        }
        rbracket();
    }

     void appendCollectionFieldValue(Collection<?> value) {
        lbracket();
        Iterator<?> iterator = value.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (i == SEQUENCE_LIMIT) {
                append("...");
                break;
            }
            append(iterator.next());
            if (i < value.size() - 1) {
                comma();
                space();
            }

            i++;
        }
        rbracket();
     }

    public void appendInaccessible() {
        append("[?]");
        newline();
        if (mode == Mode.INLINE) {
            comma();
            space();
        } else if (mode.isMultiline()) {
            newline();
        }
    }

    public void appendAfterException(Exception e) {
        append("[ERROR]");
        space();
        append(e.getClass().getSimpleName());
        newline();
        if (mode == Mode.INLINE) {
            comma();
            space();
        } else if (mode.isMultiline()) {
            newline();
        }
    }

    public boolean globalLimitReached() {
        return delegate.length() >= 10_000;
    }

    public void appendSuffix() {
        if (mode == Mode.INLINE) {
            rbracket();
        }
    }

}
