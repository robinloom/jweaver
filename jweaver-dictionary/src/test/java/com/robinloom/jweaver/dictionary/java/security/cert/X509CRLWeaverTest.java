package com.robinloom.jweaver.dictionary.java.security.cert;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.security.auth.x500.X500Principal;
import java.security.cert.X509CRL;

class X509CRLWeaverTest extends TypeWeaverTest {

    private final X509CRLWeaver weaver = new X509CRLWeaver();

    @Test
    public void test() throws Exception {
        X509CRL crl = Mockito.mock(X509CRL.class);
        Mockito.when(crl.getIssuerX500Principal()).thenReturn(new X500Principal("CN=Dummy, OU=Dummy, O=Dummy, SERIALNUMBER=1"));
        Mockito.when(crl.getSigAlgName()).thenReturn("SHA256withRSA");

        Assertions.assertEquals("X509CRL[issuer=Dummy (Dummy), version=0, algorithm=SHA256withRSA, entries=0]",
                                weaver.weave(crl, ctx));
    }
}
