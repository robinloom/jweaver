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
package com.robinloom.jweaver;

import com.robinloom.jweaver.inline.InlineWeaver;
import com.robinloom.jweaver.tree.TreeWeaver;

/**
 * Default implementation of {@link ReflectionWeaverResolver}.
 * <p>
 * The {@code ModeDispatcher} provides a mapping from {@link Mode} values
 * to corresponding {@link Weaver} implementations. It selects the appropriate
 * rendering strategy for reflective object processing based on the requested
 * output mode.
 * <p>
 * Each invocation of {@link #resolve(Mode)} returns a {@link Weaver} instance
 * suitable for the given mode. Implementations are stateless and may be
 * created per call.
 * <p>
 * This class follows a singleton pattern and is intended to be reused.
 */
public class ModeDispatcher implements ReflectionWeaverResolver {

    private static class Holder {
        private static final ModeDispatcher INSTANCE = new ModeDispatcher();
    }

    public static ModeDispatcher getInstance() {
        return Holder.INSTANCE;
    }

    private ModeDispatcher() {}

    /**
     * Returns a new {@link Weaver} instance suitable for the given mode.
     * <p>
     * The returned weaver is stateless and may be instantiated per call.
     *
     * @param mode the mode to resolve a weaver for
     * @return a corresponding {@link Weaver} implementation
     */
    @Override
    public Weaver resolve(Mode mode) {
        return switch (mode) {
            case TREE -> new TreeWeaver();
            case null, default -> new InlineWeaver();
        };
    }
}
