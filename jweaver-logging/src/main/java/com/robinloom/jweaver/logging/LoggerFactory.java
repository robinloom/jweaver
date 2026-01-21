/*
 * Copyright (C) 2025 Robin Kösters
 * mail[at]robinloom[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.robinloom.jweaver.logging;

import com.robinloom.jweaver.JWeaver;
import com.robinloom.jweaver.commons.Weaver;
import com.robinloom.jweaver.linear.LinearWeaver;
import org.slf4j.Logger;

/**
 * A JWeaver specific LoggerFactory.
 * This class is used to get a {@link JWeaverLogger} wrapping the provided {@link Logger}.
 */
public class LoggerFactory {

    /**
     * Gets a {@link JWeaverLogger} pre-initialized with a {@link LinearWeaver}.
     * @param clazz class to return the Logger for
     * @return the JWeaverLogger wrapping a SLF4J Logger
     */
    public static Logger getLogger(Class<?> clazz) {
        Logger delegate = org.slf4j.LoggerFactory.getLogger(clazz);
        return new JWeaverLogger(delegate, JWeaver.Advanced.linear());
    }

    /**
     * Gets a {@link JWeaverLogger} using the provided Weaver.
     * @param clazz class to return the Logger for
     * @param weaver the Weaver to use for printing objects
     * @return the JWeaverLogger wrapping a SLF4J Logger
     */
    public static Logger getLogger(Class<?> clazz, Weaver weaver) {
        Logger delegate = org.slf4j.LoggerFactory.getLogger(clazz);
        return new JWeaverLogger(delegate, weaver);
    }

    /**
     * Gets a {@link JWeaverLogger} pre-initialized with a {@link LinearWeaver}.
     * @param name name of the SLF4J Logger
     * @return the JWeaverLogger wrapping a SLF4J Logger
     */
    public static Logger getLogger(String name) {
        Logger delegate = org.slf4j.LoggerFactory.getLogger(name);
        return new JWeaverLogger(delegate, JWeaver.Advanced.linear());
    }

    /**
     * Gets a {@link JWeaverLogger} using the provided Weaver.
     * @param name name of the SLF4J Logger
     * @param weaver the Weaver to use for printing objects
     * @return the JWeaverLogger wrapping a SLF4J Logger
     */
    public static Logger getLogger(String name, Weaver weaver) {
        Logger delegate = org.slf4j.LoggerFactory.getLogger(name);
        return new JWeaverLogger(delegate, weaver);
    }
}
