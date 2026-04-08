package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.loom.Loom;

import java.util.Date;

public class DateWeaver implements TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Date.class;
    }

    @Override
    public String weave(Object object, WeavingContext context) {
        if (object == null) {
            return "null";
        }

        Date date = (Date) object;

        return Loom.with(Date.class.getSimpleName())
                   .lbracket()
                   .append(date.toInstant())
                   .rbracket()
                   .toString();
    }
}