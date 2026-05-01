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
import org.jspecify.annotations.NonNull;

import java.io.ByteArrayOutputStream;

public class ByteArrayOutputStreamWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return ByteArrayOutputStream.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext context) {
        ByteArrayOutputStream out = (ByteArrayOutputStream) object;
        return "ByteArrayOutputStream[size=" + out.size() + "]";
    }
}
