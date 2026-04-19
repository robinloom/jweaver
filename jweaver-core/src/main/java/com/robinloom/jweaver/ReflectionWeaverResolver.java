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

/**
 * Resolves a {@link Weaver} implementation for a given {@link Mode}.
 * <p>
 * A {@code ReflectionWeaverResolver} acts as a factory or dispatcher that
 * selects the appropriate {@link Weaver} responsible for rendering complex
 * objects according to the specified output mode.
 * <p>
 * It is used when no specialized {@link TypeWeaver} is available for a value,
 * providing a mode-specific fallback for reflective rendering.
 * <p>
 * Implementations are expected to be lightweight and may return new or shared
 * {@link Weaver} instances.
 */
public interface ReflectionWeaverResolver {

    /**
     * Returns a {@link Weaver} suitable for the given mode.
     *
     * @param mode the output mode
     * @return a corresponding {@link Weaver} implementation
     */
    Weaver resolve(Mode mode);
}
