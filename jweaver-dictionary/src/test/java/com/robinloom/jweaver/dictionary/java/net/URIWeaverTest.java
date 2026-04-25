package com.robinloom.jweaver.dictionary.java.net;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;

class URIWeaverTest extends TypeWeaverTest {

    private final URIWeaver weaver = new URIWeaver();

    @Test
    void test() {
        String expected = "URI[https://www.robinloom.com]";

        Assertions.assertEquals(expected, weaver.weave(URI.create("https://www.robinloom.com"), ctx));
    }
}