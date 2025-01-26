package com.robinloom.jweaver;

import java.lang.reflect.Field;

public class DefaultWeaver {

    private final WeaverConfig config;
    private final WeavingMachine machine;

    DefaultWeaver() {
        this.config = new WeaverConfig();
        this.machine = new WeavingMachine(config);
    }

    public DefaultWeaver classNamePrefix(String classNamePrefix) {
        config.setClassNamePrefix(classNamePrefix);
        return this;
    }

    public DefaultWeaver classNameSuffix(String classNameSuffix) {
        config.setClassNameSuffix(classNameSuffix);
        return this;
    }

    public DefaultWeaver fieldValueSeparator(String separator) {
        config.setFieldValueSeparator(separator);
        return this;
    }

    public DefaultWeaver fieldSeparator(String separator) {
        config.setFieldSeparator(separator);
        return this;
    }

    public DefaultWeaver globalSuffix(String suffix) {
        config.setGlobalSuffix(suffix);
        return this;
    }

    public DefaultWeaver capitalizeFields() {
        config.setCapitalizeFields(true);
        return this;
    }

    public String weave(Object object) {
        machine.appendClassName(object.getClass().getSimpleName());
        for (Field field : object.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);

                machine.appendFieldName(field);

                if (TypeChecks.isLogFriendly(field)) {
                    machine.appendFieldValue(field.get(object));
                } else if (TypeChecks.isLogFriendlyArray(field)) {
                    machine.appendFieldValue(TypeStrings.array(field.get(object)));
                } else {
                    machine.appendFieldValue(TypeStrings.identity(field.get(object)));
                }
            } catch (ReflectiveOperationException e) {
                machine.appendInaccessible();
            } catch (Exception e) {
                machine.appendAfterException(e);
            }
        }
        machine.appendSuffix();

        return machine.toString();
    }

}
