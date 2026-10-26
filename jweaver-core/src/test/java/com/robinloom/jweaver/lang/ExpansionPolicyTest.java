package com.robinloom.jweaver.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

class ExpansionPolicyTest {

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
}
