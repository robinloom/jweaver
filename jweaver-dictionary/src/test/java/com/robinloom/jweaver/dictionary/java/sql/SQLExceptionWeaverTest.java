package com.robinloom.jweaver.dictionary.java.sql;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class SQLExceptionWeaverTest extends TypeWeaverTest {

    private final SQLExceptionWeaver weaver = new SQLExceptionWeaver();

    @Test
    void testWeaveWithSqlStateAndErrorCode() {
        SQLException e = new SQLException("db failed", "42000", 1064);

        String result = weaver.weave(e, ctx);

        Assertions.assertTrue(result.startsWith("SQLException: db failed (at "));
        Assertions.assertTrue(result.endsWith(") [sqlState=42000, errorCode=1064]"));
    }

    @Test
    void testWeaveWithoutSqlStateOrErrorCodeFallsBackToPlainThrowableStyle() {
        SQLException e = new SQLException("db failed");

        String result = weaver.weave(e, ctx);

        Assertions.assertTrue(result.startsWith("SQLException: db failed (at "));
        Assertions.assertFalse(result.contains("sqlState"));
        Assertions.assertFalse(result.contains("errorCode"));
    }

    @Test
    void testCauseChainStillWorks() {
        SQLException cause = new SQLException("connection reset");
        SQLException e = new SQLException("db failed", "08006", cause);

        String result = weaver.weave(e, ctx);

        Assertions.assertTrue(result.contains("[Caused by: SQLException: connection reset"));
        Assertions.assertTrue(result.contains("sqlState=08006"));
    }
}
