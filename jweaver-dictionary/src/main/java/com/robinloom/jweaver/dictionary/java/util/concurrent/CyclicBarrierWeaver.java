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
package com.robinloom.jweaver.dictionary.java.util.concurrent;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CyclicBarrier;

/**
 * {@link TypeWeaver} for {@link CyclicBarrier}.
 * <p>
 * {@code CyclicBarrier} does not override {@code toString()} at all, rendering
 * as a bare identity hashcode with no state. This weaver surfaces the two
 * public facts that actually matter for debugging: how many parties the
 * barrier expects, and how many are currently waiting.
 */
public class CyclicBarrierWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return CyclicBarrier.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        CyclicBarrier barrier = (CyclicBarrier) object;

        return "CyclicBarrier[" + "parties=" + barrier.getParties() + ", " +
                                  "waiting=" + barrier.getNumberWaiting() +
                             "]";
    }
}
