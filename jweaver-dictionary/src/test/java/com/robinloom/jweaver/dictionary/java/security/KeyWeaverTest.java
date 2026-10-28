package com.robinloom.jweaver.dictionary.java.security;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECKey;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;

class KeyWeaverTest extends TypeWeaverTest {

    KeyWeaver weaver = new KeyWeaver();

    @Test
    void weave_rsa_key() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(4096);

        KeyPair keyPair = gen.generateKeyPair();

        String publicKeyResult = weaver.weave(keyPair.getPublic(), ctx);

        Assertions.assertEquals("PublicKey[RSA, 4096 bits, ", publicKeyResult.substring(0, publicKeyResult.lastIndexOf(",")+2));

        String privateKeyResult = weaver.weave(keyPair.getPrivate(), ctx);

        Assertions.assertEquals("PrivateKey[RSA, 4096 bits, ", privateKeyResult.substring(0, privateKeyResult.lastIndexOf(",")+2));
    }

    @Test
    void weave_ec_key() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
        gen.initialize(521);

        KeyPair keyPair = gen.generateKeyPair();

        String publicKeyResult = weaver.weave(keyPair.getPublic(), ctx);

        Assertions.assertEquals("PublicKey[EC, secp521r1, ", publicKeyResult.substring(0, publicKeyResult.lastIndexOf(",")+2));

        String privateKeyResult = weaver.weave(keyPair.getPrivate(), ctx);

        Assertions.assertEquals("PrivateKey[EC, secp521r1, ", privateKeyResult.substring(0, privateKeyResult.lastIndexOf(",")+2));
    }

    @Test
    void weave_dsa_key() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("DSA");
        gen.initialize(1024);

        KeyPair keyPair = gen.generateKeyPair();

        String publicKeyResult = weaver.weave(keyPair.getPublic(), ctx);

        Assertions.assertEquals("PublicKey[DSA, 1024 bits, ", publicKeyResult.substring(0, publicKeyResult.lastIndexOf(",")+2));

        String privateKeyResult = weaver.weave(keyPair.getPrivate(), ctx);

        Assertions.assertEquals("PrivateKey[DSA, 1024 bits, ", privateKeyResult.substring(0, privateKeyResult.lastIndexOf(",")+2));
    }

    @Test
    void weave_symmetric_key_uses_default_switch_branch_and_generic_type_label() throws Exception {
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(128);
        SecretKey secretKey = gen.generateKey();

        String result = weaver.weave(secretKey, ctx);

        Assertions.assertTrue(result.startsWith("Key[AES, "),
                "A key that is neither a PublicKey/PrivateKey nor EC/RSA/DSA must fall back to the generic 'Key' label "
                        + "and skip the algorithm-specific switch branches");
    }

    @Test
    void weave_key_with_no_encoding_and_no_algorithm() {
        Key bareKey = new Key() {
            @Override
            public String getAlgorithm() {
                return null;
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return null;
            }
        };

        Assertions.assertEquals("Key[?, null]", weaver.weave(bareKey, ctx));
    }

    @Test
    void weave_key_whose_getEncoded_throws_still_degrades_gracefully() {
        Key throwingKey = new Key() {
            @Override
            public String getAlgorithm() {
                return "FAKE";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                throw new RuntimeException("boom");
            }
        };

        Assertions.assertEquals("Key[FAKE, null]", weaver.weave(throwingKey, ctx));
    }

    @Test
    void tryGetCurveName_falls_back_to_bit_length_for_an_unrecognized_curve() {
        EllipticCurve curve = new EllipticCurve(
                new ECFieldFp(BigInteger.valueOf(23)),
                BigInteger.valueOf(1),
                BigInteger.valueOf(1));
        ECParameterSpec customSpec = new ECParameterSpec(
                curve, new ECPoint(BigInteger.valueOf(3), BigInteger.valueOf(10)), BigInteger.valueOf(29), 1);
        ECKey fakeKey = () -> customSpec;

        String result = weaver.tryGetCurveName(fakeKey);

        Assertions.assertEquals("5 bits", result,
                "A curve with no matching named ECGenParameterSpec must fall back to reporting its field size");
    }
}
