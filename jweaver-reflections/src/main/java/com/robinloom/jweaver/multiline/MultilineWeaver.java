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
package com.robinloom.jweaver.multiline;

import com.robinloom.jweaver.inline.InlineWeaver;

/**
 * LinearWeaver generates a string representation for a given object by combining
 * the object information (class name, field names, values) with a set of separator strings.
 * The set of separator strings is dynamically configurable.
 * <p>
 * Example:
 * <pre>
 * Person[name=John Doe, birthday=1990-01-01]
 * </pre>
 */
public class MultilineWeaver extends InlineWeaver {

    public MultilineWeaver() {}

    @Override
    protected String fieldDelimiter() {
        return "\n";
    }

    @Override
    protected String fieldValueDelimiter() {
        return ": ";
    }

    @Override
    protected String opening() {
        return "\n";
    }

    @Override
    protected String closing() {
        return "";
    }

}
