package com.robinloom.jweaver.dictionary;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.dictionary.java.sql.SQLExceptionWeaver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class DictionaryTest {

    @Test
    void testUnregisteredValueTypeFallsBackToToStringWeaver() {
        TypeWeaver weaver = Dictionary.getInstance().resolve(Integer.class);

        Assertions.assertInstanceOf(ToStringWeaver.class, weaver);
    }

    @Test
    void testMoreSpecificWeaverWinsOverGenericOne() {
        TypeWeaver weaver = Dictionary.getInstance().resolve(SQLException.class);

        Assertions.assertInstanceOf(SQLExceptionWeaver.class, weaver,
                "SQLException must resolve to the dedicated SQLExceptionWeaver, not the generic ThrowableWeaver, "
                        + "even though both target types match");
    }
}
