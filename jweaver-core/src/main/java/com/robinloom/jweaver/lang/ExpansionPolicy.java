package com.robinloom.jweaver.lang;

import org.jspecify.annotations.Nullable;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**

 * Heuristic policy that determines whether a given type should be excluded
 * from reflective expansion.
 *
 * <p>The goal of this policy is to prevent unsafe, expensive, or semantically
 * misleading traversals when inspecting object graphs. Instead of relying on
 * explicit configuration, this class applies a set of pragmatic rules based on
 * common Java and ecosystem patterns.</p>
 *
 * <p>Types are excluded from expansion if they fall into one of the following categories:</p>
 * <ul>
 *     <li><strong>Value types</strong> – simple, self-contained types that should not be expanded further</li>
 *     <li><strong>Consumables</strong> – types that represent one-time or stateful resources (e.g. streams, iterators, futures)</li>
 *     <li><strong>Proxies</strong> – dynamically generated classes (e.g. CGLIB, ByteBuddy, JDK proxies)</li>
 *     <li><strong>JDK internals</strong> – low-level or reflective infrastructure classes</li>
 *     <li><strong>Infrastructure types</strong> – framework or runtime components (e.g. Spring, Hibernate, thread pools)</li>
 * </ul>
 *
 * <p>Container types such as arrays, {@link Iterable}, and {@link Map} are explicitly
 * allowed, as they represent structural data rather than infrastructure.</p>
 *
 * <p>This policy is intentionally conservative and may evolve over time as new
 * edge cases are discovered.</p>
 */
public final class ExpansionPolicy {

    private ExpansionPolicy() {}

    /**
     * Determines whether the given type should <strong>not</strong> be expanded.
     *
     * <p>A return value of {@code true} indicates that the type should be treated
     * as a leaf and not traversed reflectively.</p>
     *
     * @param type the type to evaluate
     * @return {@code true} if the type should not be expanded, {@code false} otherwise
     */
    public static boolean shouldNotExpand(@Nullable Class<?> type) {

        if (type == null) return true;

        if (ValueTypes.isValueType(type)) {
            return true;
        }

        if (isContainer(type)) {
            return false;
        }

        if (isConsumable(type)) {
            return true;
        }

        if (isLikelyProxy(type)) {
            return true;
        }

        if (isJdkInternal(type)) {
            return true;
        }

        return isLikelyInfrastructure(type);
    }

    private static boolean isContainer(Class<?> type) {
        return type.isArray()
                || Iterable.class.isAssignableFrom(type)
                || Map.class.isAssignableFrom(type)
                || Map.Entry.class.isAssignableFrom(type);
    }

    private static boolean isConsumable(Class<?> type) {
        return Stream.class.isAssignableFrom(type)
                || Iterator.class.isAssignableFrom(type)
                || Spliterator.class.isAssignableFrom(type)
                || Future.class.isAssignableFrom(type)
                || CompletionStage.class.isAssignableFrom(type)
                || InputStream.class.isAssignableFrom(type)
                || Reader.class.isAssignableFrom(type);
    }

    private static boolean isLikelyProxy(Class<?> type) {
        String name = type.getName();

        return Proxy.isProxyClass(type)
                || type.isSynthetic()
                || name.contains("$$")
                || name.contains("EnhancerBy")
                || name.contains("$Proxy")
                || name.contains("ByteBuddy")
                || name.contains("CGLIB");
    }

    private static boolean isJdkInternal(Class<?> type) {
        String pkg = type.getPackageName();

        return pkg.startsWith("java.lang.reflect")
                || pkg.startsWith("sun.")
                || pkg.startsWith("com.sun.");
    }

    private static boolean isLikelyInfrastructure(Class<?> type) {
        String name = type.getName();

        return name.contains("hibernate")
                || name.contains("springframework")
                || name.contains("EntityManager")
                || name.contains("PersistenceContext")
                || name.contains("DataSource")
                || name.contains("HttpClient")
                || name.contains("ExecutorService")
                || name.contains("ThreadPool");
    }
}
