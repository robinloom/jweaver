package com.robinloom.jweaver.dictionary;

import org.jspecify.annotations.Nullable;

public interface TypeWeaver {

    boolean supports(Class<?> clazz);

    String weave(@Nullable Object object, WeavingContext context);
}
