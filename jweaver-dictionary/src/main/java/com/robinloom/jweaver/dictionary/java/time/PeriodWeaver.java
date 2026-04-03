package com.robinloom.jweaver.dictionary.java.time;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;
import org.jspecify.annotations.Nullable;

import java.time.Period;

public class PeriodWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).exactly(Period.class);
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "";
        }

        Period p = (Period) object;

        return "Period[" + p.getYears() + "y " + p.getMonths() + "m " + p.getDays() + "d]";
    }
}
