package com.robinloom.jweaver.dictionary.java.security.cert;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;
import com.robinloom.loom.Loom;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.security.auth.x500.X500Principal;
import java.security.cert.X509Certificate;
import java.util.*;

public class X509CertificateWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).subclassOf(X509Certificate.class);
    }

    @Override
    public String weave(Object object, WeavingContext context) {
        X509Certificate cert = (X509Certificate) object;

        String subject = formatSubject(cert.getSubjectX500Principal());
        String issuer = formatSubject(cert.getIssuerX500Principal());

        String notBefore = formatDate(cert.getNotBefore());
        String notAfter = formatDate(cert.getNotAfter());

        Loom loom = Loom.with("X509Certificate");
        loom.bracket(() -> {
                loom.keyEqValue("subject", subject);
                loom.commaSpace();
                loom.keyEqValue("issuer", issuer);
                loom.commaSpace();
                loom.append("validity");
                loom.eq();
                loom.append(notBefore).space().append("-").space().append(notAfter);
        });

        return loom.toString();
    }

    private String formatSubject(X500Principal principal) {
        Map<String, String> dn = parseDn(principal);

        String cn = dn.get("CN");
        String o  = dn.get("O");
        String mail = dn.get("MAIL");
        String c  = dn.get("C");
        String sn = dn.get("SERIALNUMBER");

        StringBuilder sb = new StringBuilder();

        if (cn != null) {
            sb.append(cn);
        }

        List<String> extras = new ArrayList<>();

        if (o != null) extras.add(o);
        if (mail != null) extras.add(mail);
        if (c != null) extras.add(c);
        if (sn != null) extras.add(sn);

        if (!extras.isEmpty()) {
            sb.append(" (");
            sb.append(String.join(", ", extras));
            sb.append(")");
        }

        return sb.toString();
    }

    private Map<String, String> parseDn(X500Principal principal) {
        try {
            LdapName ldap = new LdapName(principal.getName());

            Map<String, String> result = new LinkedHashMap<>();

            for (Rdn rdn : ldap.getRdns()) {
                result.put(rdn.getType(), rdn.getValue().toString());
            }

            return result;
        } catch (Exception e) {
            return Map.of("DN", principal.getName());
        }
    }

    private String formatDate(Date date) {
        return date.toInstant().toString();
    }
}
