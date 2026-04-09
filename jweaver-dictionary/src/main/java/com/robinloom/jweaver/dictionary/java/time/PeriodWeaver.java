package com.robinloom.jweaver.dictionary.java.time;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.time.Period;

public class PeriodWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Period.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Period p = (Period) object;

        return Loom.with(Period.class.getSimpleName())
                   .lbracket()
                   .append(p.getYears())
                   .append("y").space()
                   .append(p.getMonths())
                   .append("m").space()
                   .append(p.getDays())
                   .append("d")
                   .rbracket()
                   .toString();
    }
}
