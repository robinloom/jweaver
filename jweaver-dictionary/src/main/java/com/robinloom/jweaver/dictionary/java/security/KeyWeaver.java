package com.robinloom.jweaver.dictionary.java.security;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;

import java.security.*;
import java.security.interfaces.*;
import java.security.spec.ECGenParameterSpec;

public class KeyWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).subclassOf(Key.class) && Classes.is(clazz).noSubclassOf(javax.crypto.SecretKey.class);
    }

    @Override
    public String weave(Object object, WeavingContext context) {
        Key key = (Key) object;

        String type = typeOf(key);
        String algorithm = safe(key.getAlgorithm());

        StringBuilder sb = new StringBuilder();
        sb.append(type).append("[").append(algorithm);

        switch (key) {
            case ECKey ecKey -> sb.append(", ").append(tryGetCurveName(ecKey));
            case RSAKey rsaKey -> sb.append(", ").append(rsaBits(rsaKey));
            case DSAKey dsaKey -> sb.append(", ").append(dsaBits(dsaKey));
            default -> {
            }
        }

        sb.append(", ").append(fingerprint(key));

        sb.append("]");

        return sb.toString();
    }

    private String typeOf(Key key) {
        if (key instanceof PublicKey) {
            return "PublicKey";
        }
        if (key instanceof PrivateKey) {
            return "PrivateKey";
        }
        return "Key";
    }

    private String rsaBits(RSAKey key) {
        return key.getModulus().bitLength() + " bits";
    }

    public String tryGetCurveName(ECKey key) {
        try {
            AlgorithmParameters params = AlgorithmParameters.getInstance("EC");
            params.init(key.getParams());
            return params.getParameterSpec(ECGenParameterSpec.class).getName();
        }  catch (Exception e) {
            return ecBits(key);
        }
    }

    private String ecBits(ECKey key) {
        return key.getParams().getCurve().getField().getFieldSize() + " bits";
    }

    private String dsaBits(DSAKey key) {
        return key.getParams().getP().bitLength() + " bits";
    }

    private String fingerprint(Key key) {
        try {
            byte[] encoded = key.getEncoded();
            if (encoded == null) {
                return null;
            }

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(encoded);

            return toHex(hash);
        } catch (Exception e) {
            return null;
        }
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte aByte : bytes) {
            sb.append(String.format("%02X", aByte));
            sb.append(":");
        }

        return sb.toString();
    }

    private String safe(String value) {
        return value != null ? value : "?";
    }
}
