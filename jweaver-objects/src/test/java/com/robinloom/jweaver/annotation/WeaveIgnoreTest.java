package com.robinloom.jweaver.annotation;

import com.robinloom.jweaver.JWeaver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WeaveIgnoreTest {

    record Account(String name, @WeaveIgnore String password) {
    }

    @Test
    void testDynamicWeaver() {
        WeaveIgnoreTest.Account account = new WeaveIgnoreTest.Account("John0815", "password");
        String expected = "Account[name=John0815]";
        Assertions.assertEquals(expected, JWeaver.Internal.flat().weave(account));
    }

    @Test
    void testTreeWeaver() {
        WeaveIgnoreTest.Account account = new WeaveIgnoreTest.Account("John0815", "password");
        String expected = """
                          Account
                          `-- name=John0815""";
        Assertions.assertEquals(expected, JWeaver.Internal.tree().weave(account));
    }

    @Test
    void testBulletWeaver() {
        WeaveIgnoreTest.Account account = new WeaveIgnoreTest.Account("John0815", "password");
        String expected = """
                          Account
                           - name=John0815""";
        Assertions.assertEquals(expected, JWeaver.Internal.bullet().weave(account));
    }

    @Test
    void testCardWeaver() {
        WeaveIgnoreTest.Account account = new WeaveIgnoreTest.Account("John0815", "password");
        String expected = """
                         ╭ Account ────────╮
                         │ name : John0815 │
                         ╰─────────────────╯""";
        Assertions.assertEquals(expected, JWeaver.Internal.card().weave(account));
    }
}
