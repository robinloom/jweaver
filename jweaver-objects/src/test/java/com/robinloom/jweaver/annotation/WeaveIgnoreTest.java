package com.robinloom.jweaver.annotation;

import com.robinloom.jweaver.JWeaver;
import com.robinloom.jweaver.Mode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WeaveIgnoreTest {

    record Account(String name, @WeaveIgnore String password) {
    }

    @Test
    void testLinearWeaver() {
        WeaveIgnoreTest.Account account = new WeaveIgnoreTest.Account("John0815", "password");
        String expected = "Account[name=John0815]";
        Assertions.assertEquals(expected, JWeaver.weave(account, Mode.INLINE));
    }

    @Test
    void testTreeWeaver() {
        WeaveIgnoreTest.Account account = new WeaveIgnoreTest.Account("John0815", "password");
        String expected = """
                          Account
                          `-- name=John0815""";
        Assertions.assertEquals(expected, JWeaver.weave(account, Mode.TREE));
    }

    @Test
    void testBulletWeaver() {
        WeaveIgnoreTest.Account account = new WeaveIgnoreTest.Account("John0815", "password");
        String expected = """
                          Account
                           - name=John0815""";
        Assertions.assertEquals(expected, JWeaver.weave(account, Mode.BULLET));
    }

    @Test
    void testCardWeaver() {
        WeaveIgnoreTest.Account account = new WeaveIgnoreTest.Account("John0815", "password");
        String expected = """
                         ╭ Account ────────╮
                         │ name : John0815 │
                         ╰─────────────────╯""";
        Assertions.assertEquals(expected, JWeaver.weave(account, Mode.CARD));
    }
}
