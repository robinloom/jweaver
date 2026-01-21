package com.robinloom.jweaver.annotation;

import com.robinloom.jweaver.JWeaver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WeaveRedactTest {

    record Account(String name, @WeaveRedact String password) {
    }

    @Test
    void testDynamicWeaver() {
        Account account = new Account("John0815", "password");
        String expected = "Account[name=John0815, password=***]";
        Assertions.assertEquals(expected, JWeaver.Advanced.flat().weave(account));
    }

    @Test
    void testTreeWeaver() {
        Account account = new Account("John0815", "password");
        String expected = """
                          Account
                          |-- name=John0815
                          `-- password=***""";
        Assertions.assertEquals(expected, JWeaver.Advanced.tree().weave(account));
    }

    @Test
    void testBulletWeaver() {
        Account account = new Account("John0815", "password");
        String expected = """
                          Account
                           - name=John0815
                           - password=***""";
        Assertions.assertEquals(expected, JWeaver.Advanced.bullet().weave(account));
    }

    @Test
    void testCardWeaver() {
        Account account = new Account("John0815", "password");
        String expected = """
                         ╭ Account ────────────╮
                         │ name     : John0815 │
                         │ password : ***      │
                         ╰─────────────────────╯""";
        Assertions.assertEquals(expected, JWeaver.Advanced.card().weave(account));
    }
}
