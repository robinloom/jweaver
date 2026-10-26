package com.robinloom.jweaver.dictionary.java.security;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.Provider;
import java.security.Security;

class ProviderWeaverTest extends TypeWeaverTest {

    private final ProviderWeaver weaver = new ProviderWeaver();

    @Test
    void testWeave() {
        Provider provider = Security.getProviders()[0];

        String result = weaver.weave(provider, ctx);

        Assertions.assertEquals(
                "Provider[name=" + provider.getName() + ", version=" + provider.getVersionStr()
                        + ", services=" + provider.size() + "]",
                result);
    }
}
