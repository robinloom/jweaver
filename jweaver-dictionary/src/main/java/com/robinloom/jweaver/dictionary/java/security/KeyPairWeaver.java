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
package com.robinloom.jweaver.dictionary.java.security;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import org.jspecify.annotations.NonNull;

import java.security.KeyPair;

/**
 * {@link TypeWeaver} for {@link KeyPair}.
 * <p>
 * Delegates rendering of both keys back to the {@link WeavingContext}, so the
 * algorithm/bit-length/fingerprint detail already provided by {@link KeyWeaver}
 * is reused rather than duplicated.
 */
public class KeyPairWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return KeyPair.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        KeyPair keyPair = (KeyPair) object;

        return "KeyPair[" + "public=" + ctx.weave(keyPair.getPublic()) + ", " +
                            "private=" + ctx.weave(keyPair.getPrivate()) +
                       "]";
    }
}
