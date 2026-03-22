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
package com.robinloom.jweaver.inline;

import com.robinloom.jweaver.annotation.WeaveIgnore;
import com.robinloom.jweaver.annotation.WeaveName;
import com.robinloom.jweaver.commons.Weaver;
import com.robinloom.jweaver.util.FieldOperations;
import com.robinloom.jweaver.util.SensitivityDetection;
import com.robinloom.jweaver.util.TypeDictionary;
import com.robinloom.loom.Chars;
import com.robinloom.loom.Loom;

import java.lang.reflect.Array;
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
public class InlineWeaver implements Weaver {

    protected static final ThreadLocal<Set<Object>> history
            = ThreadLocal.withInitial(() -> Collections.newSetFromMap(new IdentityHashMap<>()));

    public InlineWeaver() {}

    /**
     * Generates a string representation of the given object via reflections.
     * Prints the class name followed by every accessible field.
     * For JDK classes, a regular <code>toString()</code> result is returned.
     * Detects reciprocal and circular object dependencies.
     * @param object object to generate a string representation for
     * @return a well-structured, human-readable representation of that object
     */
    public String weave(Object object) {
        if (object == null) {
            return "null";
        }
        if (TypeDictionary.isJdkType(object.getClass())) {
            return object.toString();
        }

        if (history.get().contains(object)) {
            history.remove();
            return "";
        } else {
            history.get().add(object);
        }

        List<Field> fields = FieldOperations.getFields(object.getClass());

        fields = fields.stream()
                       .filter(f -> !f.isAnnotationPresent(WeaveIgnore.class))
                       .toList();

        String fieldDelimiter = fieldDelimiter();
        String fieldValueDelimiter = fieldValueDelimiter();

        Loom loom = Loom.create();
        loom.append(object.getClass().getSimpleName())
                .append(opening())
                .join(fieldDelimiter, fields, field -> {
                    try {
                        Object value = readField(field, object);

                        String fieldName;
                        if (field.isAnnotationPresent(WeaveName.class)) {
                            fieldName = field.getAnnotation(WeaveName.class).value();
                        } else {
                            fieldName = field.getName();
                        }
                        String woven;

                        if (SensitivityDetection.isSensitive(field)) {
                            woven = Chars.repeat(Chars.ASTERISK, 3);
                        } else if (TypeDictionary.isCollection(field.getType())) {
                            woven = weaveCollection((Collection<?>) value);
                        } else if (TypeDictionary.isArray(field.getType())) {
                            woven = weaveArray(value);
                        } else  {
                            woven = value.toString();
                        }

                        return fieldName + fieldValueDelimiter + woven;
                    } catch (Exception ex) {
                        return Chars.LBRACE + Chars.QUESTION_MARK + Chars.RBRACE;
                    }
                }).append(closing());

        if (history.get().size() == 1) {
            history.remove();
        }

        return loom.toString();
    }

    Object readField(Field field, Object target) throws ReflectiveOperationException {
        field.setAccessible(true);
        return field.get(target);
    }

    private String weaveArray(Object array) {
        Loom loom = Loom.create();
        loom.bracket(() -> loom.join(", ", Loom.range(0, Array.getLength(array)), i -> Array.get(array, i)));
        return loom.toString();
    }

    private String weaveCollection(Collection<?> collection) {
        Loom loom = Loom.create();
        loom.bracket(() -> loom.join(", ", collection, e -> e));
        return loom.toString();
    }

    protected String fieldDelimiter() {
        return ", ";
    }

    protected String fieldValueDelimiter() {
        return "=";
    }

    protected String opening() {
        return "[";
    }

    protected String closing() {
        return "]";
    }

}
