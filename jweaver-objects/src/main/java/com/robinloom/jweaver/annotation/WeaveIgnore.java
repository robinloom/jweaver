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
package com.robinloom.jweaver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excludes a field, method, or record component from being rendered by JWeaver.
 * <p>
 * Use {@code @WeaveIgnore} to suppress properties that are not relevant for
 * textual output or that would otherwise clutter the rendered representation.
 * This annotation is evaluated by all {@link com.robinloom.jweaver.commons.Weaver}
 * implementations and takes precedence over other weave-related annotations.
 * <p>
 * Typical examples include internal caches, derived values, or references that
 * would cause redundant output.
 *
 * <pre>{@code
 * public class User {
 *     private String name;
 *
 *     @WeaveIgnore
 *     private Session currentSession;
 * }
 * }</pre>
 *
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WeaveIgnore {
}
