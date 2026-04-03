package com.robinloom.jweaver.dictionary.java.security.cert;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static org.junit.jupiter.api.Assertions.*;

class X509CertificateWeaverTest extends TypeWeaverTest {

    private final X509CertificateWeaver weaver = new X509CertificateWeaver();

    @Test
    void supports_x509_certificate() {
        assertTrue(weaver.supports(X509Certificate.class));
        assertFalse(weaver.supports(Object.class));
    }

    @Test
    void weave_basic_certificate() throws Exception {
        X509Certificate cert = testCert();

        String result = weaver.weave(cert, context);

        assertEquals("X509Certificate[subject=Dummy (Dummy, DE), issuer=Dummy (Dummy, DE), validity=2026-04-02T19:46:18Z - 2027-04-02T19:46:18Z]", result);
    }

    private X509Certificate testCert() throws Exception {
        String pem = """
                -----BEGIN CERTIFICATE-----
                MIIFxzCCA6+gAwIBAgIUFQHTuFfbJxB4z8gVgBeT/2c6yY4wDQYJKoZIhvcNAQEL
                BQAwczELMAkGA1UEBhMCREUxDjAMBgNVBAgMBUR1bW15MQ4wDAYDVQQHDAVEdW1t
                eTEOMAwGA1UECgwFRHVtbXkxDjAMBgNVBAsMBUR1bW15MQ4wDAYDVQQDDAVEdW1t
                eTEUMBIGCSqGSIb3DQEJARYFRHVtbXkwHhcNMjYwNDAyMTk0NjE4WhcNMjcwNDAy
                MTk0NjE4WjBzMQswCQYDVQQGEwJERTEOMAwGA1UECAwFRHVtbXkxDjAMBgNVBAcM
                BUR1bW15MQ4wDAYDVQQKDAVEdW1teTEOMAwGA1UECwwFRHVtbXkxDjAMBgNVBAMM
                BUR1bW15MRQwEgYJKoZIhvcNAQkBFgVEdW1teTCCAiIwDQYJKoZIhvcNAQEBBQAD
                ggIPADCCAgoCggIBALdZVBpkusV+t3SaMa8fTqwf4+82nDQCCZj85zadHANy9fsF
                1wZ8HKNBEzQV2RHwth3o7kXJTZeoWsJ3YBFj5e4KnRDuw5JzJnzemXx0M3o5NmnP
                m8juceWd5L8ixGj0UvSRljuKD2kiDpm4RlCfNk0TtKSJPa/iIE3L+2lL1sIixvK2
                kmLePDR8RmXJRT1FbeupBSuLYPNMReSrgOZZhBW9HHzodKr0vxxHWiYVQKQCMA2B
                MKQgrvzds921naW11MTHCGhCjOp82g99RU6JQKxwzdeo28RWHnCuluGoM8pdwCNZ
                tSpKwfzHmBRJW+7RPI0EI/N+ls8mUNT0F2lFofwx+/GRjArBt+qe76Z43KbkpMBv
                J0b5b021eNH7AAO84J4eONFtx9Iy1Oznr7sd6p8FwGpc1tIGNqVDHi91RKlN9jnY
                3PKLG29T2yE2t2na1V2HkK5f218MUTOeWeGBO+GzTBZX+FmVnpm8xTWUU+FTB8IX
                oZw6v4YtiWvnBQmmqY+EpJEg4Ozk5wyLslzPk/ePZFw+m3j6cLxK3YOUNES0ycUg
                vz//WIllIHXEhqvf8qM/q1pjnQkagWdQATNHrs+2q7uyM0JSOM3SEQdS/E3XvKkK
                FjOXogmHJQZgNtdOnRG82NUkvAfu7Sxm/rlfypM+LdtCQbz3pdi9CWDTekiDAgMB
                AAGjUzBRMB0GA1UdDgQWBBTJy/AEo+mOKTrQ7cDygflFR3wa9DAfBgNVHSMEGDAW
                gBTJy/AEo+mOKTrQ7cDygflFR3wa9DAPBgNVHRMBAf8EBTADAQH/MA0GCSqGSIb3
                DQEBCwUAA4ICAQBABexdqdNfWV3FHn9nhJQoyBRPSyVskvo2kxNVxB5XPDn35F10
                XoIQsf9Ryz7KMmthMtMoL1A/l3kaZVI4SNZfeBsTd8798xjubkJqTZn3wb5YcTQt
                fwcZqimZPUnvKG3hqn4ANyJ6rDijKSPaeaaKr3/gxFU9Fi/QQlN0l2Yp1fOVmXq0
                ERkPJCEWUlK32wjTSLzGsfbeow5sx3PURDBgthT+FVj8gOI+fYEtKMPjWdVmR+qD
                arbsKwr6S+402Ke7ZhsbwxKxGAeS0Pi6yIQ7rwcwrTOuzqlxD31sK56rnOA61nEE
                RPRfG2OuMsTxE9PLRoMsToS/4lNqMaGIbjSImOipU/gahf1o8sv+d+hVi+25ogHP
                Bq8Z0voPkTxyPbDxONKcVhKStpN+x3lHgoPPIxL7yR7gLc1EeJ1hphF0chmTB4Fx
                3bnfbmL5vtuDHqq+D55J/Y2mYMn/TX7fY+7BLeiHOb08c/09DGJqAWlwxbU30lT6
                +1QAVi93Df8ttx/Nt+FwLKPORNicloRRhdlBbXq2JpNBa5qWXYgSD3hQJ312pfes
                JSE3abVHW7x/nWU+TdS2uEtWvEsoJaexFtEJCk+yUNEJXaE3phSeEgmYgCqSoAKc
                EsImILLy+RTbAOrYD22aHy1feSsUmoX4wlFJs2mKMY6rcifb7+HbR0KaIg==
                -----END CERTIFICATE-----
                """;

        InputStream in = new ByteArrayInputStream(pem.getBytes());

        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) factory.generateCertificate(in);
    }
}
