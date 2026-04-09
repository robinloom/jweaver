package com.robinloom.jweaver;

public interface TypeWeaverResolver {

    TypeWeaver resolve(Class<?> clazz);
}
