package com.robinloom.jweaver.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class SensitivityDetectionTest {

    @SuppressWarnings("unused")
    private static class Sample {
        String password;
        String Password;
        String PASSWORD;
        String authToken;
        String clientSecret;
        String apiKey;
        String privateKey;
        String accessToken;
        String refreshToken;
        String sessionId;
        String creditCardNumber;
        String ssn;
        String SSN;
        String iban;
        String cvv;
        String pin;

        String secretary;
        String authorId;
        String primaryKey;
        String mapKey;
        String cardType;
        String tokenizer;
        String name;
        String email;
    }

    private Field field(String name) throws NoSuchFieldException {
        return Sample.class.getDeclaredField(name);
    }

    private void assertSensitive(String fieldName) throws NoSuchFieldException {
        Assertions.assertTrue(SensitivityDetection.isSensitive(field(fieldName)),
                "Expected '" + fieldName + "' to be detected as sensitive");
    }

    private void assertNotSensitive(String fieldName) throws NoSuchFieldException {
        Assertions.assertFalse(SensitivityDetection.isSensitive(field(fieldName)),
                "Expected '" + fieldName + "' to NOT be detected as sensitive");
    }

    @Test
    void detectsExactLegacyNames() throws NoSuchFieldException {
        assertSensitive("password");
        assertSensitive("apiKey");
        assertSensitive("privateKey");
    }

    @Test
    void detectsCaseVariants() throws NoSuchFieldException {
        assertSensitive("Password");
        assertSensitive("PASSWORD");
        assertSensitive("SSN");
    }

    @Test
    void detectsCommonRealWorldPatternsMissedByExactMatching() throws NoSuchFieldException {
        assertSensitive("authToken");
        assertSensitive("clientSecret");
        assertSensitive("accessToken");
        assertSensitive("refreshToken");
        assertSensitive("sessionId");
        assertSensitive("creditCardNumber");
        assertSensitive("ssn");
        assertSensitive("iban");
        assertSensitive("cvv");
        assertSensitive("pin");
    }

    @Test
    void doesNotFlagUnrelatedFieldsWithMatchingSubstrings() throws NoSuchFieldException {
        assertNotSensitive("secretary");
        assertNotSensitive("authorId");
        assertNotSensitive("primaryKey");
        assertNotSensitive("mapKey");
        assertNotSensitive("cardType");
        assertNotSensitive("tokenizer");
        assertNotSensitive("name");
        assertNotSensitive("email");
    }

    @Test
    void byteAndCharArraysAreAlwaysSensitiveRegardlessOfName() throws NoSuchFieldException {
        @SuppressWarnings("unused")
        class WithArrays {
            byte[] payload;
            char[] buffer;
        }

        Assertions.assertTrue(SensitivityDetection.isSensitive(WithArrays.class.getDeclaredField("payload")));
        Assertions.assertTrue(SensitivityDetection.isSensitive(WithArrays.class.getDeclaredField("buffer")));
    }

    @Test
    void fieldAnnotatedWeaveRedactIsAlwaysSensitiveRegardlessOfName() throws NoSuchFieldException {
        @SuppressWarnings("unused")
        class WithRedactedField {
            @com.robinloom.jweaver.annotation.WeaveRedact
            String comment;
        }

        Assertions.assertTrue(SensitivityDetection.isSensitive(WithRedactedField.class.getDeclaredField("comment")));
    }

    @Test
    void leadingSeparatorInFieldNameDoesNotProduceAFalseNegative() throws NoSuchFieldException {
        @SuppressWarnings("unused")
        class WithLeadingSeparator {
            String _secret;
        }

        Assertions.assertTrue(SensitivityDetection.isSensitive(WithLeadingSeparator.class.getDeclaredField("_secret")),
                "A leading separator must not prevent the empty leading token from being filtered out");
    }
}
