package com.robinloom.jweaver.dictionary.java.security.cert;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.jweaver.util.Classes;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.security.auth.x500.X500Principal;
import java.security.cert.X509CRL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class X509CRLWeaver implements TypeWeaver {

    @Override
    public boolean supports(Class<?> clazz) {
        return Classes.is(clazz).subclassOf(X509CRL.class);
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "";
        }

        X509CRL crl = (X509CRL) object;

        String subject = formatSubject(crl.getIssuerX500Principal());

        return Loom.create().append("X509CRL")
                            .lbracket()
                            .keyEqValue("issuer", subject)
                            .commaSpace()
                            .keyEqValue("version", crl.getVersion())
                            .commaSpace()
                            .keyEqValue("algorithm", crl.getSigAlgName())
                            .commaSpace()
                            .keyEqValue("entries", crl.getRevokedCertificates() != null ? crl.getRevokedCertificates().size() : 0)
                            .rbracket()
                            .toString();
    }

    private String formatSubject(X500Principal principal) {
        Map<String, String> dn = parseDn(principal);

        String cn = dn.get("CN");
        String o  = dn.get("O");
        String mail = dn.get("MAIL");
        String c  = dn.get("C");

        StringBuilder sb = new StringBuilder();

        if (cn != null) {
            sb.append(cn);
        }

        List<String> extras = new ArrayList<>();

        if (o != null) extras.add(o);
        if (mail != null) extras.add(mail);
        if (c != null) extras.add(c);

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
}
