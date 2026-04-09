package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.util.Date;

public class DateWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Date.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Date date = (Date) object;

        return Loom.with(Date.class.getSimpleName())
                   .lbracket()
                   .append(date.toInstant())
                   .rbracket()
                   .toString();
    }
}