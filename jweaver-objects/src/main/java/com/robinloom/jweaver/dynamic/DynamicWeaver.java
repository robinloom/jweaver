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
package com.robinloom.jweaver.dynamic;

import com.robinloom.jweaver.commons.Weaver;
import com.robinloom.jweaver.util.FieldOperations;
import com.robinloom.jweaver.util.TypeDictionary;

import java.lang.reflect.Field;
import java.util.*;

/**
 * DynamicWeaver generates a string representation for a given object by combining
 * the object information (class name, field names, values) with a set of separator strings.
 * The set of separator strings is dynamically configurable.
 * <p>
 * Example:
 * <pre>
 * Person[name=John Doe, birthday=1990-01-01]
 * </pre>
 */
public class DynamicWeaver implements Weaver {

    protected static final ThreadLocal<Set<Object>> history
            = ThreadLocal.withInitial(() -> Collections.newSetFromMap(new IdentityHashMap<>()));

    private final DynamicConfig config;
    private final DynamicWeavingMachine machine;

    public DynamicWeaver() {
        this.config = new DynamicConfig();
        this.machine = new DynamicWeavingMachine(config);
    }

    /**
     * Sets newline '\n' as separator for fields and for classname and fields.
     * The result will be a multiline output.
     * @return instance for chaining
     */
    public DynamicWeaver multiline() {
        config.setFieldSeparator("\n");
        config.setClassNameFieldsSeparator("\n");
        config.setGlobalSuffix("");
        return this;
    }

    /**
     * Sets a string to be placed before the class name.
     * Default is empty string.
     * @param classNamePrefix the string to use
     * @return instance for chaining
     */
    public DynamicWeaver classNamePrefix(String classNamePrefix) {
        config.setClassNamePrefix(classNamePrefix);
        return this;
    }

    /**
     * Sets a string to be placed after the class name.
     * Default is empty string.
     * @param classNameSuffix the string to use
     * @return instance for chaining
     */
    public DynamicWeaver classNameSuffix(String classNameSuffix) {
        config.setClassNameSuffix(classNameSuffix);
        return this;
    }

    /**
     * Sets a string to separate the class name and the fields to be printed.
     * Will be between the class name suffix and the first field.
     * Default string is "[".
     * @param separator the separator string to use
     * @return instance for chaining
     */
    public DynamicWeaver classNameFieldsSeparator(String separator) {
        config.setClassNameFieldsSeparator(separator);
        return this;
    }

    /**
     * Sets a string to separate the field name and the corresponding value.
     * Default string is "=".
     * @param separator the separator string to use
     * @return instance for chaining
     */
    public DynamicWeaver fieldValueSeparator(String separator) {
        config.setFieldValueSeparator(separator);
        return this;
    }

    /**
     * Sets a string that is used to separate the printed fields.
     * Default string is ","
     * @param separator the separator string to use.
     * @return instance for chaining
     */
    public DynamicWeaver fieldSeparator(String separator) {
        config.setFieldSeparator(separator);
        return this;
    }

    /**
     * Sets a string that is placed at the end of the generated string.
     * Default string is "]".
     * @param suffix the string to be used.
     * @return instance for chaining.
     */
    public DynamicWeaver globalSuffix(String suffix) {
        config.setGlobalSuffix(suffix);
        return this;
    }

    /**
     * Sets the maximum length of collections and arrays to be included in the output.
     * Elements that exceed the limit will be summarized (... 57 more).
     * @param maxSequenceLength an integer value
     * @return instance for chaining
     */
    public DynamicWeaver maxSequenceLength(int maxSequenceLength) {
        config.setMaxSequenceLength(maxSequenceLength);
        return this;
    }

    /**
     * Sets the names of fields that should be included in the output.
     * By default, every field is included.
     * @param fields list of strings containing included field names
     * @return instance for chaining
     */
    public DynamicWeaver includeFields(List<String> fields) {
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
    public DynamicWeaver excludeFields(List<String> fields) {
        config.setExcludedFields(fields);
        config.setIncludedFields(List.of());
        return this;
    }

    /**
     * Determines if the class name should be omitted when printing.
     * By default, the class name is included.
     * @return instance for chaining
     */
    public DynamicWeaver omitClassName() {
        config.setOmitClassName(true);
        return this;
    }

    /**
     * Enables capitalization of field names.
     * firstName -> FirstName
     * @return instance for chaining
     */
    public DynamicWeaver capitalizeFields() {
        config.setCapitalizeFields(true);
        return this;
    }

    /**
     * Enables the printing of data types
     * @return instance for chaining
     */
    public DynamicWeaver showDataTypes() {
        config.setShowDataTypes(true);
        return this;
    }

    /**
     * Activates the inclusion of inherited fields.
     * @return instance for chaining
     */
    public DynamicWeaver showInheritedFields() {
        config.setShowInheritedFields(true);
        return this;
    }

    /**
     * Will order field names alphabetically before printing.
     * @return instance for chaining
     */
    public DynamicWeaver orderFieldsAlphabetically() {
        config.setOrderFieldsAlphabetically(true);
        return this;
    }

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
            return "";
        } else {
            history.get().add(object);
        }

        if (config.isIncludeClassName()) {
            machine.appendClassName(object.getClass().getSimpleName());
        }

        List<Field> fields;
        if (config.isShowInheritedFields()) {
            fields = FieldOperations.getAllFields(object.getClass());
        } else {
            fields = FieldOperations.getFields(object.getClass());
        }

        if (config.isOrderFieldsAlphabetically()) {
            fields.sort(Comparator.comparing(Field::getName));
        }

        fields = fields.stream()
                       .filter(config::isIncluded)
                       .toList();

        for (Field field : fields) {
            if (machine.globalLimitReached()) {
                break;
            }
            try {
                field.setAccessible(true);
                Object value = field.get(object);

                machine.appendDataType(field);
                machine.appendFieldName(field);

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
        return machine.toString();
    }

}
