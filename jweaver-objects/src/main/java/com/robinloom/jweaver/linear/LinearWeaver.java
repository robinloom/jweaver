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
import com.robinloom.jweaver.annotation.WeaveIgnore;
import com.robinloom.jweaver.annotation.WeaveName;
import com.robinloom.jweaver.commons.Weaver;
import com.robinloom.jweaver.util.FieldOperations;
import com.robinloom.jweaver.util.SensitivityDetection;
import com.robinloom.jweaver.util.TypeDictionary;

import java.lang.reflect.Field;
import java.util.*;

/**
 * LinearWeaver generates a string representation for a given object by combining
 * the object information (class name, field names, values) with a set of separator strings.
 * The set of separator strings is dynamically configurable.
 * <p>
 * Example:
 * <pre>
 * Person[name=John Doe, birthday=1990-01-01]
 * </pre>
 */
public class LinearWeaver implements Weaver {

    protected static final ThreadLocal<Set<Object>> history
            = ThreadLocal.withInitial(() -> Collections.newSetFromMap(new IdentityHashMap<>()));

    public LinearWeaver() {}

    public String weave(Object object) {
        return weave(object, Mode.INLINE);
    }

    /**
     * Generates a string representation of the given object via reflections.
     * Prints the class name followed by every accessible field.
     * For JDK classes, a regular <code>toString()</code> result is returned.
     * Detects reciprocal and circular object dependencies.
     * @param object object to generate a string representation for
     * @return a well-structured, human-readable representation of that object
     */
    public String weave(Object object, Mode mode) {
        LinearWeavingMachine machine = new LinearWeavingMachine(mode);

        if (object == null) {
            return "null";
        }
        if (TypeDictionary.isJdkType(object.getClass())) {
            return object.toString();
        }

        if (history.get().contains(object)) {
            return "";
        } else {
            history.get().add(object);
        }

        machine.appendClassName(object.getClass().getSimpleName());

        List<Field> fields;
        if (mode == Mode.MULTILINE_VERBOSE) {
            fields = FieldOperations.getAllFields(object.getClass());
        } else {
            fields = FieldOperations.getFields(object.getClass());
        }

        fields = fields.stream()
                       .filter(f -> !f.isAnnotationPresent(WeaveIgnore.class))
                       .toList();

        for (Field field : fields) {
            if (machine.globalLimitReached()) {
                break;
            }
            try {
                field.setAccessible(true);
                Object value = field.get(object);
                if (SensitivityDetection.isSensitive(field)) {
                    value = "***";
                }

                machine.appendDataType(field);
                if (field.isAnnotationPresent(WeaveName.class)) {
                    machine.appendFieldName(field.getAnnotation(WeaveName.class).value());
                } else {
                    machine.appendFieldName(field.getName());
                }

                if (TypeDictionary.isArray(field.getType())) {
                    machine.appendArrayFieldValue(value);
                } else if (TypeDictionary.isCollection(field.getType())) {
                    machine.appendCollectionFieldValue((Collection<?>) value);
                } else {
                    machine.appendFieldValue(value, fields.indexOf(field) == fields.size() - 1);
                }
            } catch (ReflectiveOperationException e) {
                machine.appendInaccessible();
            } catch (Exception e) {
                machine.appendAfterException(e);
            }
        }
        machine.appendSuffix();

        if (history.get().size() == 1) {
            history.remove();
        }

        String result = machine.toString();
        machine.reset();
        return result;
    }

}
