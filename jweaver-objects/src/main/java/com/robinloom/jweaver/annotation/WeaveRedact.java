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
 * Masks the value of a field or record component when rendered by JWeaver.
 * <p>
 * Use {@code @WeaveRedact} to hide sensitive information such as passwords,
 * API keys, tokens, or personally identifiable data. Redacted values will
 * be replaced by a sequence of mask characters (for example {@code ***}).
 * <p>
 *
 * <pre>{@code
 * public class Credentials {
 *     private String username;
 *
 *     @WeaveRedact(maskString = '•')
 *     private String password;
 * }
 * }</pre>
 *
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WeaveRedact {
    String maskString() default "***";
}
