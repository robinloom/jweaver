package com.robinloom.jweaver.lang;

import java.util.Currency;
import java.util.Locale;
import java.util.UUID;

/**
 * Utility for identifying value-like types that should be treated as atomic
 * and not expanded further during reflective traversal.
 *
 * <p>Value types are considered self-contained representations whose
 * {@code toString()} output is typically sufficient and whose internal
 * structure does not provide additional meaningful insight.</p>
 *
 * <p>This includes:</p>
 * <ul>
 *     <li>Primitive types and their wrapper classes</li>
 *     <li>{@link CharSequence} implementations (e.g. {@link String})</li>
 *     <li>{@link Number} types</li>
 *     <li>{@link Enum} types</li>
 *     <li>Types from the {@code java.time} package</li>
 *     <li>Common JDK value objects such as {@link UUID}, {@link Currency}, and {@link Locale}</li>
 * </ul>
 *
 * <p>This classification is heuristic and may evolve as additional value-like
 * types are identified.</p>
 */
public final class ValueTypes {

    /**
     * Determines whether the given type should be treated as a value type.
     *
     * <p>A value type is considered a terminal node during traversal and
     * should not be expanded further.</p>
     *
     * @param type the type to evaluate (may be {@code null})
     * @return {@code true} if the type is considered a value type, {@code false} otherwise
     */
    public static boolean isValueType(Class<?> type) {
        if (type == null) return false;

        if (type.isPrimitive()) return true;
        if (isWrapper(type)) return true;

        if (CharSequence.class.isAssignableFrom(type)) return true;

        if (type.isEnum()) return true;

        if (Number.class.isAssignableFrom(type)) return true;

        if (isJavaTime(type)) return true;

        return type == UUID.class
                || type == Currency.class
                || type == Locale.class;
    }

    private static boolean isWrapper(Class<?> type) {
        return type == Boolean.class
                || type == Byte.class
                || type == Short.class
                || type == Integer.class
                || type == Long.class
                || type == Float.class
                || type == Double.class
                || type == Character.class;
    }

    private static boolean isJavaTime(Class<?> type) {
        return type.getPackageName().startsWith("java.time");
    }
}
