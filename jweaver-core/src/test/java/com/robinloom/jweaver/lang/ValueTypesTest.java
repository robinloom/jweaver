package com.robinloom.jweaver.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Locale;
import java.util.UUID;

class ValueTypesTest {

    private enum Color { RED, GREEN, BLUE }

    @Test
    void testNullIsNotAValueType() {
        Assertions.assertFalse(ValueTypes.isValueType(null));
    }

    @Test
    void testPrimitiveTypesAreValueTypes() {
        Assertions.assertTrue(ValueTypes.isValueType(int.class));
        Assertions.assertTrue(ValueTypes.isValueType(boolean.class));
        Assertions.assertTrue(ValueTypes.isValueType(double.class));
    }

    @Test
    void testWrapperTypesAreValueTypes() {
        Assertions.assertTrue(ValueTypes.isValueType(Boolean.class));
        Assertions.assertTrue(ValueTypes.isValueType(Byte.class));
        Assertions.assertTrue(ValueTypes.isValueType(Short.class));
        Assertions.assertTrue(ValueTypes.isValueType(Integer.class));
        Assertions.assertTrue(ValueTypes.isValueType(Long.class));
        Assertions.assertTrue(ValueTypes.isValueType(Float.class));
        Assertions.assertTrue(ValueTypes.isValueType(Double.class));
        Assertions.assertTrue(ValueTypes.isValueType(Character.class));
    }

    @Test
    void testCharSequenceImplementationsAreValueTypes() {
        Assertions.assertTrue(ValueTypes.isValueType(String.class));
        Assertions.assertTrue(ValueTypes.isValueType(StringBuilder.class));
    }

    @Test
    void testEnumTypesAreValueTypes() {
        Assertions.assertTrue(ValueTypes.isValueType(Color.class));
    }

    @Test
    void testNumberTypesAreValueTypes() {
        Assertions.assertTrue(ValueTypes.isValueType(BigInteger.class));
        Assertions.assertTrue(ValueTypes.isValueType(BigDecimal.class));
    }

    @Test
    void testJavaTimeTypesAreValueTypes() {
        Assertions.assertTrue(ValueTypes.isValueType(LocalDate.class));
        Assertions.assertTrue(ValueTypes.isValueType(Instant.class));
        Assertions.assertTrue(ValueTypes.isValueType(Duration.class));
    }

    @Test
    void testKnownJdkValueObjectsAreValueTypes() {
        Assertions.assertTrue(ValueTypes.isValueType(UUID.class));
        Assertions.assertTrue(ValueTypes.isValueType(Currency.class));
        Assertions.assertTrue(ValueTypes.isValueType(Locale.class));
    }

    @Test
    void testArbitraryObjectIsNotAValueType() {
        class Point {
            @SuppressWarnings("unused")
            int x;
        }

        Assertions.assertFalse(ValueTypes.isValueType(Point.class));
    }
}
