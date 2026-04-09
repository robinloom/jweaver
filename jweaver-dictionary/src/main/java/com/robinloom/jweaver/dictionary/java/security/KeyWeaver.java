package com.robinloom.jweaver.dictionary.java.security;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.security.*;
import java.security.interfaces.DSAKey;
import java.security.interfaces.ECKey;
import java.security.interfaces.RSAKey;
import java.security.spec.ECGenParameterSpec;

public class KeyWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return Key.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Key key = (Key) object;

        String type = typeOf(key);
        String algorithm = safe(key.getAlgorithm());

        Loom loom = Loom.with(type, "[", algorithm);

        switch (key) {
            case ECKey ecKey -> loom.commaSpace().append(tryGetCurveName(ecKey));
            case RSAKey rsaKey -> loom.commaSpace().append(rsaBits(rsaKey));
            case DSAKey dsaKey -> loom.commaSpace().append(dsaBits(dsaKey));
            default -> {
            }
        }

        loom.commaSpace()
            .append(fingerprint(key))
            .rbracket();

        return loom.toString();
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
