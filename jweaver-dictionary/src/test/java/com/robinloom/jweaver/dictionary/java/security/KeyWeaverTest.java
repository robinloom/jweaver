package com.robinloom.jweaver.dictionary.java.security;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

class KeyWeaverTest extends TypeWeaverTest {

    KeyWeaver weaver = new KeyWeaver();

    @Test
    void weave_rsa_key() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(4096);

        KeyPair keyPair = gen.generateKeyPair();

        String publicKeyResult = weaver.weave(keyPair.getPublic(), context);

        Assertions.assertEquals("PublicKey[RSA, 4096 bits, ", publicKeyResult.substring(0, publicKeyResult.lastIndexOf(",")+2));

        String privateKeyResult = weaver.weave(keyPair.getPrivate(), context);

        Assertions.assertEquals("PrivateKey[RSA, 4096 bits, ", privateKeyResult.substring(0, privateKeyResult.lastIndexOf(",")+2));
    }

    @Test
    void weave_ec_key() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
        gen.initialize(521);

        KeyPair keyPair = gen.generateKeyPair();

        String publicKeyResult = weaver.weave(keyPair.getPublic(), context);

        Assertions.assertEquals("PublicKey[EC, secp521r1, ", publicKeyResult.substring(0, publicKeyResult.lastIndexOf(",")+2));

        String privateKeyResult = weaver.weave(keyPair.getPrivate(), context);

        Assertions.assertEquals("PrivateKey[EC, secp521r1, ", privateKeyResult.substring(0, privateKeyResult.lastIndexOf(",")+2));
    }

    @Test
    void weave_dsa_key() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("DSA");
        gen.initialize(1024);

        KeyPair keyPair = gen.generateKeyPair();

        String publicKeyResult = weaver.weave(keyPair.getPublic(), context);

        Assertions.assertEquals("PublicKey[DSA, 1024 bits, ", publicKeyResult.substring(0, publicKeyResult.lastIndexOf(",")+2));

        String privateKeyResult = weaver.weave(keyPair.getPrivate(), context);

        Assertions.assertEquals("PrivateKey[DSA, 1024 bits, ", privateKeyResult.substring(0, privateKeyResult.lastIndexOf(",")+2));
    }
}
