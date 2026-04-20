package com.robinloom.jweaver.fields;

import java.util.*;

public final class TypeNormalizer {

    private TypeNormalizer() {}

    /**
     * Returns a normalized, human-friendly type for display purposes.
     * <p>
     * This avoids leaking JVM-specific implementation classes like
     * ImmutableCollections$List12 or Arrays$ArrayList.
     */
    public static Class<?> normalize(Class<?> type) {
        if (type == null) {
            return Object.class;
        }

        if (Collection.class.isAssignableFrom(type)) {
            return normalizeCollection(type);
        }

        if (Map.class.isAssignableFrom(type)) {
            return Map.class;
        }

        if (type.isArray()) {
            return type.getComponentType();
        }

        return type;
    }

    private static Class<?> normalizeCollection(Class<?> type) {
        if (List.class.isAssignableFrom(type)) {
            return List.class;
        }
        if (Set.class.isAssignableFrom(type)) {
            return Set.class;
        }

        return Collection.class;
    }
}
