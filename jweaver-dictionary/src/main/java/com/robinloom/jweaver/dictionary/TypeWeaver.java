package com.robinloom.jweaver.dictionary;

import org.jspecify.annotations.Nullable;

public interface TypeWeaver {

    Class<?> targetType();

    String weave(@Nullable Object object, WeavingContext context);
}
