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
package com.robinloom.jweaver.dictionary.java.sql;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.jweaver.dictionary.java.lang.ThrowableWeaver;
import org.jspecify.annotations.NonNull;

import java.sql.SQLException;

/**
 * {@link TypeWeaver} for {@link SQLException}.
 * <p>
 * Reuses {@link ThrowableWeaver}'s message/frame/cause-chain rendering for the
 * generic part, then appends {@code sqlState}/{@code errorCode} when present -
 * both are lost by the generic {@link ThrowableWeaver} rendering today, despite
 * being central to triaging database errors.
 * <p>
 * The base rendering is obtained from a locally held {@link ThrowableWeaver}
 * instance rather than via {@link WeavingContext#weave(Object)}: since this
 * weaver is itself the most specific match for {@code SQLException}, routing
 * back through the dictionary for the same object would simply resolve to this
 * weaver again, recursing indefinitely.
 */
public class SQLExceptionWeaver extends TypeWeaver {

    private final ThrowableWeaver throwableWeaver = new ThrowableWeaver();

    @Override
    public Class<?> targetType() {
        return SQLException.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        SQLException e = (SQLException) object;
        String base = throwableWeaver.weave(object, ctx);

        StringBuilder extra = new StringBuilder();
        if (e.getSQLState() != null) {
            extra.append("sqlState=").append(e.getSQLState());
        }
        if (e.getErrorCode() != 0) {
            if (!extra.isEmpty()) {
                extra.append(", ");
            }
            extra.append("errorCode=").append(e.getErrorCode());
        }

        return extra.isEmpty() ? base : base + " [" + extra + "]";
    }
}
