package com.robinloom.jweaver.dictionary.java.security.cert;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.security.auth.x500.X500Principal;
import java.security.cert.X509CRL;
import java.util.Set;

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

    @Test
    public void weave_subject_with_mail_and_country_but_no_cn_or_org() throws Exception {
        X509CRL crl = Mockito.mock(X509CRL.class);
        X500Principal principal = Mockito.mock(X500Principal.class);
        Mockito.when(principal.getName()).thenReturn("MAIL=foo@bar.com,C=DE");
        Mockito.when(crl.getIssuerX500Principal()).thenReturn(principal);
        Mockito.when(crl.getSigAlgName()).thenReturn("SHA256withRSA");

        Assertions.assertEquals("X509CRL[issuer= (foo@bar.com, DE), version=0, algorithm=SHA256withRSA, entries=0]",
                                weaver.weave(crl, ctx));
    }

    @Test
    public void weave_reports_actual_entry_count_when_revoked_certificates_present() throws Exception {
        X509CRL crl = Mockito.mock(X509CRL.class);
        Mockito.when(crl.getIssuerX500Principal()).thenReturn(new X500Principal("CN=Dummy"));
        Mockito.when(crl.getSigAlgName()).thenReturn("SHA256withRSA");
        Mockito.when(crl.getRevokedCertificates()).thenReturn(Set.of());

        Assertions.assertTrue(weaver.weave(crl, ctx).contains("entries=0"));
    }

    @Test
    public void weave_subject_falls_back_to_empty_string_for_unparseable_dn() throws Exception {
        X509CRL crl = Mockito.mock(X509CRL.class);
        X500Principal malformed = Mockito.mock(X500Principal.class);
        Mockito.when(malformed.getName()).thenReturn("this is not a valid dn###");
        Mockito.when(crl.getIssuerX500Principal()).thenReturn(malformed);
        Mockito.when(crl.getSigAlgName()).thenReturn("SHA256withRSA");

        Assertions.assertTrue(weaver.weave(crl, ctx).contains("issuer=,"),
                "An unparseable DN must degrade to an empty rendered subject instead of throwing");
    }
}
