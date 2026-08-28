package com.robinloom.jweaver;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.PrivateKey;

import static org.junit.jupiter.api.Assertions.*;

class JWeaverTest {

    @Test
    void switchMode() {
        record Person(String name) {}

        JWeaver.switchMode(Mode.TREE);

        String expected = """
                          Person
                          `-- name="John\"""";

        assertEquals(expected, JWeaver.weave(new Person("John")));

        JWeaver.switchMode(Mode.INLINE);
    }

    @Test
    void privateKeyFieldOfNonJdkTypeIsNeverReflectivelyExpanded() {
        // Simulates a PrivateKey implementation from a third-party provider
        // (e.g. BouncyCastle), which does not live under sun.*/com.sun.* and
        // is therefore not caught by ExpansionPolicy's JDK-internal heuristic.
        class ThirdPartyPrivateKey implements PrivateKey {
            private final BigInteger privateExponent = new BigInteger("123456789987654321");

            @Override
            public String getAlgorithm() {
                return "FAKE";
            }

            @Override
            public String getFormat() {
                return "PKCS#8";
            }

            @Override
            public byte[] getEncoded() {
                return privateExponent.toByteArray();
            }
        }

        record Credentials(PrivateKey key) {}

        String output = JWeaver.weave(new Credentials(new ThirdPartyPrivateKey()));

        assertFalse(output.contains("privateExponent"),
                "Private key internals must never be reflectively expanded, regardless of provider");
        assertFalse(output.contains("123456789987654321"),
                "Raw key material must never appear in the output");
        assertTrue(output.contains("PrivateKey["),
                "The registered KeyWeaver must be used instead of falling back to raw reflection");
    }

    private static class Money {
        @SuppressWarnings("unused")
        private final long cents;

        private Money(long cents) {
            this.cents = cents;
        }
    }

    @Test
    void registerTypeWeaverIsUsedInsteadOfReflectiveExpansion() {
        JWeaver.registerTypeWeaver(new TypeWeaver() {
            @Override
            public Class<?> targetType() {
                return Money.class;
            }

            @Override
            public String weave(Object object, WeavingContext ctx) {
                return "$" + (((Money) object).cents / 100.0);
            }
        });

        record Order(Money total) {}

        String output = JWeaver.weave(new Order(new Money(1050)));

        assertTrue(output.contains("$10.5"),
                "A publicly registered TypeWeaver must be used for rendering");
        assertFalse(output.contains("cents"),
                "Once a TypeWeaver is registered, the type must no longer be reflectively expanded");
    }
}