package com.robinloom.jweaver.lang;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public final class ExpansionPolicy {

    private ExpansionPolicy() {}

    public static boolean shouldNotExpand(Class<?> type) {

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
