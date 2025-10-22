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

import com.robinloom.jweaver.annotation.WeaveName;
import com.robinloom.jweaver.annotation.WeaveRedact;
import com.robinloom.jweaver.commons.Weaver;
import com.robinloom.jweaver.util.FieldOperations;
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

    private final CardWeavingMachine machine;
    private final CardConfig config;

    public CardWeaver() {
        this.config = new CardConfig();
        this.machine = new CardWeavingMachine(config);
    }

    /**
     * Sets the names of fields that should be included in the output.
     * By default, every field is included.
     * @param fields list of strings containing included field names
     * @return instance for chaining
     */
    public CardWeaver includeFields(List<String> fields) {
        config.setIncludedFields(fields);
        config.setExcludedFields(List.of());
        return this;
    }

    /**
     * Sets the names of fields that should be excluded from the output.
     * By default, no field is excluded.
     * @param fields list of strings containing excluded field names
     * @return instance for chaining
     */
    public CardWeaver excludeFields(List<String> fields) {
        config.setExcludedFields(fields);
        config.setIncludedFields(List.of());
        return this;
    }

    /**
     * Determines if the class name should be omitted when printing.
     * By default, the class name is included.
     * @return instance for chaining
     */
    public CardWeaver omitClassName() {
        config.setOmitClassName(true);
        return this;
    }

    /**
     * Enables capitalization of field names.
     * firstName -> FirstName
     * @return instance for chaining
     */
    public CardWeaver capitalizeFields() {
        config.setCapitalizeFields(true);
        return this;
    }

    /**
     * Enables the printing of data types
     * @return instance for chaining
     */
    public CardWeaver showDataTypes() {
        config.setShowDataTypes(true);
        return this;
    }

    /**
     * Activates the inclusion of inherited fields.
     * @return instance for chaining
     */
    public CardWeaver showInheritedFields() {
        config.setShowInheritedFields(true);
        return this;
    }

    /**
     * Sets the set of characters to use for the box bordering the content
     * @param boxChars the charset to use for the card box
     * @return instance for chaining
     */
    public CardWeaver boxChars(BoxChars boxChars) {
        config.setBoxChars(boxChars);
        return this;
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
    public String weave(Object object) {
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

        LinkedHashMap<String, String> wovenFields = new LinkedHashMap<>();
        List<Field> fields;
        if (config.isShowInheritedFields()) {
            fields = FieldOperations.getAllFields(object.getClass());
        } else {
            fields = FieldOperations.getFields(object.getClass());
        }

        fields = fields.stream()
                .filter(config::isIncluded)
                .toList();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(object);

                String woven;
                Class<?> type = field.getType();
                if (field.isAnnotationPresent(WeaveRedact.class)) {
                    woven = field.getAnnotation(WeaveRedact.class).maskString();
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

                if (config.isCapitalizeFields()) {
                    fieldName = FieldOperations.capitalize(fieldName);
                }

                if (config.isShowDataTypes()) {
                    fieldName = type.getSimpleName() + " " + fieldName;
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
