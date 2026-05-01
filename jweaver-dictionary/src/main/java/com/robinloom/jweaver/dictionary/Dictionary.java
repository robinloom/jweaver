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
package com.robinloom.jweaver.dictionary;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.TypeWeaverResolver;
import com.robinloom.jweaver.dictionary.java.io.ByteArrayOutputStreamWeaver;
import com.robinloom.jweaver.dictionary.java.io.FileWeaver;
import com.robinloom.jweaver.dictionary.java.io.InputStreamWeaver;
import com.robinloom.jweaver.dictionary.java.lang.*;
import com.robinloom.jweaver.dictionary.java.net.URIWeaver;
import com.robinloom.jweaver.dictionary.java.net.URLWeaver;
import com.robinloom.jweaver.dictionary.java.nio.PathWeaver;
import com.robinloom.jweaver.dictionary.java.security.KeyWeaver;
import com.robinloom.jweaver.dictionary.java.security.cert.X509CRLWeaver;
import com.robinloom.jweaver.dictionary.java.security.cert.X509CertificateWeaver;
import com.robinloom.jweaver.dictionary.java.time.DurationWeaver;
import com.robinloom.jweaver.dictionary.java.time.PeriodWeaver;
import com.robinloom.jweaver.dictionary.java.time.TemporalWeaver;
import com.robinloom.jweaver.dictionary.java.util.DateWeaver;
import com.robinloom.jweaver.dictionary.java.util.MapEntryWeaver;
import com.robinloom.jweaver.dictionary.java.util.OptionalWeaver;
import com.robinloom.jweaver.dictionary.java.util.UUIDWeaver;
import com.robinloom.jweaver.lang.ExpansionPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of {@link TypeWeaverResolver} based on a registry of known type weavers.
 * <p>
 * The {@code Dictionary} maintains a collection of {@link TypeWeaver}s and resolves
 * the most suitable one for a given type. It is responsible for selecting specialized
 * representations for well-known types, typically improving upon default
 * {@code toString()} implementations.
 * <p>
 * Resolution follows these rules:
 * <ul>
 *     <li>All registered {@link TypeWeaver}s are considered if their target type
 *     is assignable from the requested class</li>
 *     <li>If multiple candidates match, the most specific type is selected</li>
 *     <li>If no match is found, simple types are handled by a default
 *     {@code ToStringWeaver}</li>
 *     <li>If no applicable weaver exists, {@code null} is returned</li>
 * </ul>
 * <p>
 * Results are cached per class for performance. The cache is cleared whenever
 * new {@link TypeWeaver}s are registered.
 * <p>
 * This class is implemented as a singleton and is intended to be reused.
 */
public class Dictionary implements TypeWeaverResolver {

    private static class Holder {
        private static final Dictionary INSTANCE = new Dictionary();
    }

    public static Dictionary getInstance() {
        return Holder.INSTANCE;
    }

    private static final List<TypeWeaver> RENDERERS = new ArrayList<>();
    private static final Map<Class<?>, TypeWeaver> CACHE = new ConcurrentHashMap<>();
    private static final ToStringWeaver FALLBACK = new ToStringWeaver();

    static {
        // java.io
        register(new ByteArrayOutputStreamWeaver());
        register(new FileWeaver());
        register(new InputStreamWeaver());
        // java.lang
        register(new BooleanWeaver());
        register(new ByteArrayWeaver());
        register(new CharacterWeaver());
        register(new ClassWeaver());
        register(new EnumWeaver());
        register(new StringWeaver());
        // java.net
        register(new URIWeaver());
        register(new URLWeaver());
        // java.nio
        register(new PathWeaver());
        // java.util
        register(new DateWeaver());
        register(new MapEntryWeaver());
        register(new OptionalWeaver());
        register(new UUIDWeaver());
        // java.time
        register(new DurationWeaver());
        register(new PeriodWeaver());
        register(new TemporalWeaver());
        // java.security
        register(new KeyWeaver());
        register(new X509CertificateWeaver());
        register(new X509CRLWeaver());
    }

    /**
     * Registers a new {@link TypeWeaver}.
     * <p>
     * Newly registered weavers participate in future resolution and may override
     * existing matches if they are more specific. The internal cache is cleared
     * to ensure consistent resolution.
     *
     * @param weaver the weaver to register
     */
    static void register(TypeWeaver weaver) {
        RENDERERS.add(weaver);
        CACHE.clear();
    }

    private Dictionary() {}

    /**
     * Resolves the most suitable {@link TypeWeaver} for the given class.
     *
     * @param clazz the type to resolve
     * @return a matching {@link TypeWeaver}, or {@code null} if none is applicable
     */
    @Override
    public TypeWeaver resolve(Class<?> clazz) {
        return CACHE.computeIfAbsent(clazz, this::find);
    }

    private TypeWeaver find(Class<?> clazz) {
        TypeWeaver best = findBest(clazz);
        if (best != null) {
            return best;
        }

        if (ExpansionPolicy.shouldNotExpand(clazz)) {
            return FALLBACK;
        }

        return null;
    }

    private TypeWeaver findBest(Class<?> clazz) {
        TypeWeaver best = null;

        for (TypeWeaver weaver : RENDERERS) {
            Class<?> target = weaver.targetType();

            if (target.isAssignableFrom(clazz)) {
                if (best == null || isMoreSpecific(target, best.targetType())) {
                    best = weaver;
                }
            }
        }

        return best;
    }

    private boolean isMoreSpecific(Class<?> a, Class<?> b) {
        return b.isAssignableFrom(a);
    }
}