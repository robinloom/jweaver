package com.robinloom.jweaver;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public class DynamicWeaver {

    private static final ThreadLocal<Set<Object>> history
            = ThreadLocal.withInitial(() -> Collections.newSetFromMap(new IdentityHashMap<>()));

    private final WeaverConfig config;
    private final WeavingMachine machine;

    public DynamicWeaver() {
        this.config = new WeaverConfig();
        this.machine = new WeavingMachine(config);
    }

    public DynamicWeaver multiline() {
        config.setFieldSeparator("\n");
        config.setClassNameFieldsSeparator("\n");
        config.setGlobalSuffix("");
        return this;
    }

    public DynamicWeaver classNamePrefix(String classNamePrefix) {
        config.setClassNamePrefix(classNamePrefix);
        return this;
    }

    public DynamicWeaver classNameSuffix(String classNameSuffix) {
        config.setClassNameSuffix(classNameSuffix);
        return this;
    }

    public DynamicWeaver classNameFieldsSeparator(String separator) {
        config.setClassNameFieldsSeparator(separator);
        return this;
    }

    public DynamicWeaver fieldValueSeparator(String separator) {
        config.setFieldValueSeparator(separator);
        return this;
    }

    public DynamicWeaver fieldSeparator(String separator) {
        config.setFieldSeparator(separator);
        return this;
    }

    public DynamicWeaver globalSuffix(String suffix) {
        config.setGlobalSuffix(suffix);
        return this;
    }

    public DynamicWeaver includeFields(List<String> fields) {
        config.setIncludedFields(fields);
        config.setExcludedFields(List.of());
        return this;
    }

    public DynamicWeaver excludeFields(List<String> fields) {
        config.setExcludedFields(fields);
        config.setIncludedFields(List.of());
        return this;
    }

    public DynamicWeaver capitalizeFields() {
        config.setCapitalizeFields(true);
        return this;
    }

    public DynamicWeaver showDataTypes() {
        config.setShowDataTypes(true);
        return this;
    }

    public DynamicWeaver showInheritedFields() {
        config.setShowInheritedFields(true);
        return this;
    }

    public String weave(Object object) {
        if (object == null) {
            return "null";
        }

        if (history.get().contains(object)) {
            return "";
        } else {
            history.get().add(object);
        }

        machine.appendClassName(object.getClass().getSimpleName());

        List<Field> fields;
        if (config.isShowInheritedFields()) {
            fields = FieldOperations.getAllFields(object.getClass());
        } else {
            fields = FieldOperations.getFields(object.getClass());
        }

        for (Field field : fields) {
            try {
                field.setAccessible(true);

                if (config.isExcluded(field)) {
                    continue;
                }

                Object value = field.get(object);

                machine.appendDataType(field);
                machine.appendFieldName(field);

                if (FieldOperations.isArray(field)) {
                    machine.appendFieldValue(TypeStrings.array(value));
                } else {
                    machine.appendFieldValue(value);
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
