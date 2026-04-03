package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;

import java.time.Instant;
import java.util.Date;

public class DateWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).subclassOf(Date.class);
    }

    @Override
    public String weave(Object object, WeavingContext context) {
        if (object == null) {
            return "";
        }

        Date date = (Date) object;

        Instant instant = date.toInstant();
        return "Date[" + instant + "]";
    }
}