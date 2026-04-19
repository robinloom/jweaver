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
package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return InputStream.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        InputStream in = (InputStream) object;
        String className = in.getClass().getSimpleName();
        try {
            return Loom.with(className,
                             "[remaining=", in.available(),
                            "]")
                       .toString();
        } catch (IOException e) {
            return className + "[]";
        }
    }
}
