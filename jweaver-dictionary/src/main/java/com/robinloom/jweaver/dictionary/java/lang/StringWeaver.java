/*
 * Copyright (C) 2026 Robin Kösters
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
package com.robinloom.jweaver.dictionary.java.lang;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import org.jspecify.annotations.NonNull;

/**
 * {@link TypeWeaver} for {@link CharSequence}.
 * <p>
 * Targets the interface rather than just {@link String} so that textual types
 * such as {@link StringBuilder} and {@link StringBuffer} are quoted consistently
 * with {@code String} values, instead of falling back to their raw, unquoted
 * {@code toString()}.
 * <p>
 * Nested values longer than {@value #MAX_LENGTH} characters are cut off and
 * annotated with their length, e.g. {@code "abc..." (5000 chars)}.
 */
public class StringWeaver extends TypeWeaver {

    static final int MAX_LENGTH = 1000;

    @Override
    public Class<?> targetType() {
        return CharSequence.class;
    }

    @Override
    public String weave(@NonNull Object value, WeavingContext ctx) {
        CharSequence s = value instanceof CharSequence cs ? cs : value.toString();

        if (ctx.isRoot()) {
            return s.toString();
        }

        int length = s.length();
        if (length <= MAX_LENGTH) {
            return "\"" + s + "\"";
        }

        // keep surrogate pairs intact
        int end = Character.isHighSurrogate(s.charAt(MAX_LENGTH - 1)) ? MAX_LENGTH - 1 : MAX_LENGTH;
        return "\"" + s.subSequence(0, end) + "...\" (" + length + " chars)";
    }
}
