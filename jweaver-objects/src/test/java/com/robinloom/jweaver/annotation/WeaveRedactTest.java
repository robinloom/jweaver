package com.robinloom.jweaver.annotation;

import com.robinloom.jweaver.JWeaver;
import com.robinloom.jweaver.Mode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WeaveRedactTest {

    record Account(String name, @WeaveRedact String password) {
    }

    @Test
    void testLinearWeaver() {
        Account account = new Account("John0815", "password");
        String expected = "Account[name=John0815, password=***]";
        Assertions.assertEquals(expected, JWeaver.weave(account, Mode.INLINE));
    }

    @Test
    void testTreeWeaver() {
        Account account = new Account("John0815", "password");
        String expected = """
                          Account
                          |-- name=John0815
                          `-- password=***""";
        Assertions.assertEquals(expected, JWeaver.weave(account, Mode.TREE));
    }

    @Test
    void testBulletWeaver() {
        Account account = new Account("John0815", "password");
        String expected = """
                          Account
                           - name=John0815
                           - password=***""";
        Assertions.assertEquals(expected, JWeaver.weave(account, Mode.BULLET));
    }

    @Test
    void testCardWeaver() {
        Account account = new Account("John0815", "password");
        String expected = """
                         ╭ Account ────────────╮
                         │ name     : John0815 │
                         │ password : ***      │
                         ╰─────────────────────╯""";
        Assertions.assertEquals(expected, JWeaver.weave(account, Mode.CARD));
    }
}
