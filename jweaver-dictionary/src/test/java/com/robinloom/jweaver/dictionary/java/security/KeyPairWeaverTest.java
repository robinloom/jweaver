package com.robinloom.jweaver.dictionary.java.security;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

class KeyPairWeaverTest extends TypeWeaverTest {

    private final KeyPairWeaver weaver = new KeyPairWeaver();

    @Test
    void testWeave() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        KeyPair keyPair = gen.generateKeyPair();

        String result = weaver.weave(keyPair, ctx);

        Assertions.assertTrue(result.startsWith("KeyPair[public=PublicKey[RSA, 2048 bits, "));
        Assertions.assertTrue(result.contains("], private=PrivateKey[RSA, 2048 bits, "));
        Assertions.assertTrue(result.endsWith("]]"));
    }
}
