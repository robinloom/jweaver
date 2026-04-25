package com.robinloom.jweaver.lang;

import java.util.List;
import java.util.Map;

public final class ExpansionBlacklist {

    private static final List<String> JDK_BLACKLIST = List.of(
        "java.",
        "javax.",
        "com.sun.",
        "sun."
    );

    private static final List<String> THIRD_PARTY_BLACKLIST = List.of(
            // ORM / JPA / persistence infrastructure
            "org.hibernate.",
            "org.eclipse.persistence.",
            "org.apache.openjpa.",
            "jakarta.persistence.",
            "javax.persistence.",

            // Query / SQL builder infrastructure
            "org.jooq.",
            "com.querydsl.",
            "org.mybatis.",
            "org.apache.ibatis.",

            // JDBC pools
            "com.zaxxer.hikari.",
            "org.apache.tomcat.jdbc.",
            "org.apache.commons.dbcp2.",
            "org.apache.commons.pool2.",
            "com.mchange.v2.c3p0.",
            "com.alibaba.druid.",

            // JDBC drivers
            "oracle.jdbc.",
            "com.mysql.cj.jdbc.",
            "org.mariadb.jdbc.",
            "org.postgresql.jdbc.",
            "com.microsoft.sqlserver.jdbc.",
            "com.ibm.db2.",
            "org.h2.jdbc.",
            "org.hsqldb.jdbc.",
            "org.sqlite.",

            // Servlet / web container APIs
            "jakarta.servlet.",
            "javax.servlet.",

            // Servlet containers / web server internals
            "org.apache.catalina.",
            "org.apache.coyote.",
            "org.apache.tomcat.",
            "org.eclipse.jetty.",
            "io.undertow.",

            // Spring infrastructure, web and security
            "org.springframework.context.",
            "org.springframework.beans.",
            "org.springframework.core.env.",
            "org.springframework.web.",
            "org.springframework.http.",
            "org.springframework.security.",
            "org.springframework.boot.context.",
            "org.springframework.boot.web.",
            "org.springframework.transaction.",
            "org.springframework.jdbc.",
            "org.springframework.orm.",
            "org.springframework.data.",

            // Jakarta EE / CDI / validation infrastructure
            "jakarta.enterprise.",
            "javax.enterprise.",
            "jakarta.inject.",
            "javax.inject.",
            "jakarta.validation.",
            "javax.validation.",
            "jakarta.annotation.",
            "javax.annotation.",

            // Logging implementations
            "org.slf4j.",
            "ch.qos.logback.",
            "org.apache.logging.log4j.",
            "org.apache.log4j.",

            // HTTP clients and networking runtimes
            "org.apache.http.",
            "org.apache.hc.",
            "okhttp3.",
            "retrofit2.",
            "io.netty.",
            "com.ning.http.client.",

            // Reactive runtimes / async infrastructure
            "reactor.core.",
            "reactor.netty.",
            "io.reactivex.",
            "io.smallrye.mutiny.",

            // RPC
            "io.grpc.",

            // Messaging / queues / brokers
            "jakarta.jms.",
            "javax.jms.",
            "org.apache.kafka.",
            "com.rabbitmq.",
            "org.springframework.kafka.",
            "org.springframework.amqp.",
            "org.apache.activemq.",
            "org.apache.qpid.",
            "software.amazon.awssdk.services.sqs.",
            "software.amazon.awssdk.services.sns.",

            // Redis / cache clients
            "redis.clients.",
            "io.lettuce.core.",
            "org.redisson.",
            "net.spy.memcached.",
            "com.github.benmanes.caffeine.cache.",

            // MongoDB / Elasticsearch / search clients
            "com.mongodb.",
            "org.mongodb.",
            "org.elasticsearch.",
            "co.elastic.clients.",
            "org.opensearch.",

            // Cloud SDK clients / credentials / auth-heavy infrastructure
            "software.amazon.awssdk.",
            "com.amazonaws.",
            "com.google.cloud.",
            "com.google.auth.",
            "com.azure.",
            "com.microsoft.aad.",
            "com.okta.",
            "com.auth0.",

            // OAuth / JWT / security libraries
            "org.springframework.security.oauth2.",
            "com.nimbusds.",
            "io.jsonwebtoken.",
            "org.keycloak.",
            "org.pac4j.",

            // Metrics / tracing / telemetry
            "io.micrometer.",
            "io.opentelemetry.",
            "io.opentracing.",
            "zipkin2.",
            "brave.",
            "com.codahale.metrics.",

            // Schedulers / jobs
            "org.quartz.",
            "net.javacrumbs.shedlock.",

            // Template engines
            "org.thymeleaf.",
            "freemarker.",
            "org.apache.velocity.",

            // Serialization / mapper internals
            "com.fasterxml.jackson.databind.",
            "com.fasterxml.jackson.core.",
            "com.google.gson.",
            "org.yaml.snakeyaml.",

            // Bytecode / proxy / mocking infrastructure
            "net.bytebuddy.",
            "javassist.",
            "cglib.",
            "org.mockito.",
            "org.objenesis."
    );

    private ExpansionBlacklist() {
    }

    public static boolean isBlacklisted(Class<?> type) {
        if (type == null) {
            return false;
        }

        if (type.isEnum() || Enum.class.isAssignableFrom(type)) {
            return true;
        }

        if (isContainerType(type) && !isDangerousContainerType(type)) {
            return false;
        }

        boolean blacklisted = false;

        String packageName = type.getPackageName();

        for (String entry : JDK_BLACKLIST) {
            if (packageName.startsWith(entry)) {
                blacklisted = true;
                break;
            }
        }

        for (String entry : THIRD_PARTY_BLACKLIST) {
            if (packageName.startsWith(entry)) {
                blacklisted = true;
                break;
            }
        }

        return blacklisted;
    }

    private static boolean isContainerType(Class<?> type) {
        return type.isArray()
                || Iterable.class.isAssignableFrom(type)
                || Map.class.isAssignableFrom(type)
                || Map.Entry.class.isAssignableFrom(type);
    }

    private static boolean isDangerousContainerType(Class<?> type) {
        String packageName = type.getPackageName();

        return packageName.startsWith("org.hibernate.")
                || packageName.startsWith("org.eclipse.persistence.")
                || packageName.startsWith("org.apache.openjpa.")
                || packageName.startsWith("jakarta.persistence.")
                || packageName.startsWith("javax.persistence.");
    }
}
