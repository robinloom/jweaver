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

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * {@link TypeWeaver} for {@link Throwable}, rendering a compact summary instead
 * of reflectively expanding its internal fields.
 * <p>
 * The rendered form is {@code ClassName: message (at firstStackFrame)}, followed
 * by the same representation of the cause, recursively, wrapped in
 * {@code [Caused by: ...]}.
 * <p>
 * {@link WeavingContext#weave(Object)} provides no cycle detection of its own, so
 * this weaver tracks visited instances itself and bounds the cause chain, since
 * {@link Throwable#getCause()} defaults to the throwable itself and can also be
 * made to form a genuine cycle via {@link Throwable#initCause(Throwable)}.
 */
public class ThrowableWeaver extends TypeWeaver {

    private static final int MAX_CHAIN = 8;

    @Override
    public Class<?> targetType() {
        return Throwable.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Set<Throwable> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        return render((Throwable) object, seen, 0);
    }

    private String render(Throwable t, Set<Throwable> seen, int depth) {
        seen.add(t);

        StringBuilder sb = new StringBuilder(t.getClass().getSimpleName());
        if (t.getMessage() != null) {
            sb.append(": ").append(t.getMessage());
        }

        StackTraceElement[] trace = t.getStackTrace();
        if (trace != null && trace.length > 0) {
            sb.append(" (at ").append(trace[0]).append(")");
        }

        Throwable cause = t.getCause();
        if (cause == null || cause == t) {
            return sb.toString();
        }
        if (seen.contains(cause) || depth + 1 >= MAX_CHAIN) {
            return sb.append(" [Caused by: ...]").toString();
        }

        return sb.append(" [Caused by: ").append(render(cause, seen, depth + 1)).append("]").toString();
    }
}
