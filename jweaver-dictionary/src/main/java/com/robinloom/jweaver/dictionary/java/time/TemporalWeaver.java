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
package com.robinloom.jweaver.dictionary.java.time;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

public class TemporalWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Temporal.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        return switch (object) {
            case LocalDate d -> Loom.with(LocalDate.class.getSimpleName())
                    .lbracket()
                    .append(d)
                    .rbracket()
                    .toString();
            case LocalDateTime dt -> Loom.with(LocalDateTime.class.getSimpleName())
                    .lbracket()
                    .append(dt)
                    .rbracket()
                    .toString();
            case ZonedDateTime zdt -> Loom.with(ZonedDateTime.class.getSimpleName())
                    .lbracket()
                    .append(zdt.toLocalDateTime())
                    .append(zdt.getOffset())
                    .space()
                    .append(zdt.getZone())
                    .rbracket()
                    .toString();
            case Instant i -> Loom.with(Instant.class.getSimpleName())
                    .lbracket()
                    .append(i)
                    .rbracket()
                    .toString();
            default -> object.toString();
        };

    }
}
