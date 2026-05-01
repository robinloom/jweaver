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

public class ByteArrayWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return byte[].class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        byte[] bytes = (byte[]) object;

        if (bytes.length == 0) {
            return "byte[0]";
        }

        StringBuilder sb = new StringBuilder("byte[");
        sb.append(bytes.length);
        sb.append("]: ");

        int limit = Math.min(bytes.length, 8);

        for (int i = 0; i < limit; i++) {
            sb.append(String.format("%02X", bytes[i]));
            if (i < limit - 1) {
                sb.append(" ");
            }
        }

        if (bytes.length > limit) {
            sb.append(" ..");
        }

        return sb.toString();
    }
}
