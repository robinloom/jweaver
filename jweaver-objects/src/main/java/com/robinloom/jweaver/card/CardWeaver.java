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
package com.robinloom.jweaver.card;

import com.robinloom.jweaver.Mode;
import com.robinloom.jweaver.annotation.WeaveIgnore;
import com.robinloom.jweaver.annotation.WeaveName;
import com.robinloom.jweaver.commons.Weaver;
import com.robinloom.jweaver.util.FieldOperations;
import com.robinloom.jweaver.util.SensitivityDetection;
import com.robinloom.jweaver.util.TypeDictionary;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.*;

/**
 * CardWeaver depicts a given object as a framed card
 * with aligned key-value pairs inside a box.
 * This representation focuses on clarity and aesthetics,
 * making it well-suited for logs, debug output, or summaries.
 * <p>
 * Example:
 * <pre>
 * ╭ User ───────────────╮
 * │ name  : Alice       │
 * │ age   : 28          │
 * │ email : alice@a.com │
 * ╰─────────────────────╯
 * </pre>
 */
public class CardWeaver implements Weaver {

    protected static final ThreadLocal<Set<Object>> history
            = ThreadLocal.withInitial(() -> Collections.newSetFromMap(new IdentityHashMap<>()));

    public String weave(Object object) {
        return weave(object, Mode.CARD);
    }

    /**
     * Generates a string representation of the given object via reflections.
     * Prints the class name followed by every accessible field in an ordered structure
     * resembling a business card.
     * For JDK classes, a regular <code>toString()</code> result is returned.
     * Detects reciprocal and circular object dependencies.
     * @param object object to generate a string representation for
     * @return a well-structured, human-readable representation of that object
     */
    public String weave(Object object, Mode mode) {
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

        CardWeavingMachine machine = new CardWeavingMachine();

        LinkedHashMap<String, String> wovenFields = new LinkedHashMap<>();
        List<Field> fields = FieldOperations.getFields(object.getClass());

        fields = fields.stream()
                .filter(f -> !f.isAnnotationPresent(WeaveIgnore.class))
                .toList();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(object);

                String woven;
                Class<?> type = field.getType();
                if (SensitivityDetection.isSensitive(field)) {
                    woven = "***";
                } else if (TypeDictionary.isSimpleType(value.getClass())) {
                    woven = value.toString();
                } else if (TypeDictionary.isCollection(type)) {
                    int size = FieldOperations.getCollectionSize((Collection<?>) value);
                    woven = "@"  + type.getSimpleName() + "(" + size + ")";
                } else if (TypeDictionary.isArray(type)) {
                    int size = FieldOperations.getArraySize(value);
                    woven = type.getComponentType().getSimpleName() + "[" + size + "]";
                } else {
                    woven = FieldOperations.getObjectToString(value);
                }

                String fieldName;
                if (field.isAnnotationPresent(WeaveName.class)) {
                    fieldName = field.getAnnotation(WeaveName.class).value();
                } else {
                    fieldName = field.getName();
                }

                wovenFields.put(fieldName, woven);
            } catch (InaccessibleObjectException | IllegalAccessException e) {
                wovenFields.put(field.getName(), "[?]");
            }
        }

        String clazzName = object.getClass().getSimpleName();

        machine.determineLongestField(wovenFields);
        machine.determineOverallWidth(wovenFields, clazzName);
        machine.appendClassname(clazzName);

        for (Map.Entry<String, String> entry : wovenFields.entrySet()) {
            machine.newline();
            machine.appendField(entry);
        }

        machine.appendFooter();

        return machine.toString();
    }
}
