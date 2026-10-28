package com.robinloom.jweaver.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.BitSet;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class ExpansionPolicyTest {

    @Test
    void testNullTypeShouldNotExpand() {
        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(null));
    }

    @Test
    void testValueTypeShouldNotExpand() {
        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(Integer.class));
    }

    @Test
    void testConsumableTypesShouldNotExpand() {
        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(InputStream.class));
        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(Reader.class));
        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(Stream.class));
    }

    @Test
    void testDynamicProxyShouldNotExpand() {
        Object proxy = Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[]{Runnable.class},
                (p, method, args) -> null);

        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(proxy.getClass()));
    }

    @Test
    void testLikelyInfrastructureTypesShouldNotExpand() {
        class HttpClient {}
        class ThreadPool {}

        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(HttpClient.class),
                "Class name containing a known infrastructure keyword must be excluded from expansion");
        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(ThreadPool.class),
                "Class name containing a known infrastructure keyword must be excluded from expansion");
    }

    @Test
    void testPlainJdkTypesAreNotExpanded() {
        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(OptionalInt.class));
        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(BitSet.class));
        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(GregorianCalendar.class));
        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(Pattern.class));
        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(AtomicBoolean.class));
    }

    @Test
    void testJavaxTypesAreNotExpanded() {
        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(javax.security.auth.x500.X500Principal.class));
    }

    @Test
    void testJdkInternalTypesAreNotExpanded() {
        Class<?> classLoaderImpl = ClassLoader.getSystemClassLoader().getClass();

        Assertions.assertTrue(classLoaderImpl.getPackageName().startsWith("jdk."),
                "Precondition: the system class loader's runtime type must live under jdk.*");
        Assertions.assertTrue(ExpansionPolicy.shouldNotExpand(classLoaderImpl));
    }

    @Test
    void testContainersAreStillExpandable() {
        Assertions.assertFalse(ExpansionPolicy.shouldNotExpand(List.class));
        Assertions.assertFalse(ExpansionPolicy.shouldNotExpand(int[].class));
    }

    @Test
    void testOwnClassesAreStillExpandable() {
        class Person {
            @SuppressWarnings("unused")
            String name;
        }

        Assertions.assertFalse(ExpansionPolicy.shouldNotExpand(Person.class));
    }

    @Test
    void testResultIsCachedPerClass() throws Exception {
        class Probe {
            @SuppressWarnings("unused")
            String name;
        }

        ExpansionPolicy.shouldNotExpand(Probe.class);

        Field cacheField = ExpansionPolicy.class.getDeclaredField("CACHE");
        cacheField.setAccessible(true);
        Map<?, ?> cache = (Map<?, ?>) cacheField.get(null);

        Assertions.assertTrue(cache.containsKey(Probe.class),
                "shouldNotExpand() must memoize its result per Class, since the same class is evaluated "
                        + "repeatedly for every instance encountered during traversal");
    }
}
