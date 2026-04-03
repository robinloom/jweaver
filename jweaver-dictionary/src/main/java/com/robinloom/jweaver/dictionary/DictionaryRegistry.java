package com.robinloom.jweaver.dictionary;

import com.robinloom.jweaver.dictionary.java.ArrayWeaver;
import com.robinloom.jweaver.dictionary.java.EnumWeaver;
import com.robinloom.jweaver.dictionary.java.io.FileWeaver;
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

class DictionaryRegistry {

    private static final List<TypeWeaver> RENDERERS = new ArrayList<>();
    private static final Map<Class<?>, TypeWeaver> CACHE = new ConcurrentHashMap<>();

    static {
        register(new ArrayWeaver());
        register(new CollectionWeaver());
        register(new MapEntryWeaver());
        register(new MapWeaver());
        register(new OptionalWeaver());
        register(new TemporalWeaver());
        register(new DurationWeaver());
        register(new PeriodWeaver());
        register(new DateWeaver());
        register(new X509CertificateWeaver());
        register(new X509CRLWeaver());
        register(new KeyWeaver());
        register(new FileWeaver());
        register(new PathWeaver());
        register(new EnumWeaver());
    }

    static void register(TypeWeaver weaver) {
        RENDERERS.add(weaver);
        CACHE.clear();
    }

    static TypeWeaver find(Class<?> clazz) {
        return CACHE.computeIfAbsent(clazz, DictionaryRegistry::resolve);
    }

    private static TypeWeaver resolve(Class<?> clazz) {
        for (TypeWeaver weaver : RENDERERS) {
            if (weaver.supports(clazz)) {
                return weaver;
            }
        }
        return null;
    }
}