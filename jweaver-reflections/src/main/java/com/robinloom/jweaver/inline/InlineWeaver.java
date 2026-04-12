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

import com.robinloom.jweaver.Weaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.jweaver.annotation.WeaveIgnore;
import com.robinloom.jweaver.annotation.WeaveName;
import com.robinloom.jweaver.util.FieldOperations;
import com.robinloom.jweaver.util.SensitivityDetection;
import com.robinloom.jweaver.util.Types;
import com.robinloom.loom.Chars;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

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
    public String weave(@NonNull Object object, WeavingContext ctx) {
        if (Types.isJdkType(object.getClass())) {
            return object.toString();
        }

        if (history.get().contains(object)) {
            history.remove();
            return "";
        } else {
            history.get().add(object);
        }

        List<Field> fields = FieldOperations.getFields(object.getClass()).stream()
                                            .filter(f -> !f.isAnnotationPresent(WeaveIgnore.class))
                                            .toList();

        String fieldDelimiter = fieldDelimiter();
        String fieldValueDelimiter = fieldValueDelimiter();

        Loom loom = Loom.empty();
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
                    } else  {
                        woven = weaveValue(value, ctx);
                    }

                    return fieldName + fieldValueDelimiter + woven;
                } catch (Exception ex) {
                    return "[?]";
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

    protected String weaveValue(Object value, WeavingContext ctx) {
        return ctx.weave(value);
    }

}
