package com.robinloom.jweaver.dictionary;

import com.robinloom.jweaver.dictionary.java.io.ByteArrayOutputStreamWeaver;
import com.robinloom.jweaver.dictionary.java.io.FileWeaver;
import com.robinloom.jweaver.dictionary.java.io.InputStreamWeaver;
import com.robinloom.jweaver.dictionary.java.lang.*;
import com.robinloom.jweaver.dictionary.java.nio.PathWeaver;
import com.robinloom.jweaver.dictionary.java.security.KeyWeaver;
import com.robinloom.jweaver.dictionary.java.security.cert.X509CRLWeaver;
import com.robinloom.jweaver.dictionary.java.security.cert.X509CertificateWeaver;
import com.robinloom.jweaver.dictionary.java.time.DurationWeaver;
import com.robinloom.jweaver.dictionary.java.time.PeriodWeaver;
import com.robinloom.jweaver.dictionary.java.time.TemporalWeaver;
import com.robinloom.jweaver.dictionary.java.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Dictionary {

    private static final List<TypeWeaver> RENDERERS = new ArrayList<>();
    private static final Map<Class<?>, TypeWeaver> CACHE = new ConcurrentHashMap<>();
    private static final ArrayWeaver ARRAY_WEAVER = new ArrayWeaver();

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
        // java.nio
        register(new PathWeaver());
        // java.util
        register(new CollectionWeaver());
        register(new DateWeaver());
        register(new MapEntryWeaver());
        register(new MapWeaver());
        register(new OptionalWeaver());
        // java.time
        register(new DurationWeaver());
        register(new PeriodWeaver());
        register(new TemporalWeaver());
        // java.security
        register(new KeyWeaver());
        register(new X509CertificateWeaver());
        register(new X509CRLWeaver());
    }

    static void register(TypeWeaver weaver) {
        RENDERERS.add(weaver);
        CACHE.clear();
    }

    public static TypeWeaver find(Class<?> clazz) {
        return CACHE.computeIfAbsent(clazz, Dictionary::resolve);
    }

    private static TypeWeaver resolve(Class<?> clazz) {
        TypeWeaver best = findBest(clazz);
        if (best != null) {
            return best;
        }

        if (clazz.isArray()) {
            return ARRAY_WEAVER;
        }

        return null;
    }

    private static TypeWeaver findBest(Class<?> clazz) {
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

    private static boolean isMoreSpecific(Class<?> a, Class<?> b) {
        return b.isAssignableFrom(a);
    }
}