package com.robinloom.jweaver.dictionary.java.net;

import com.robinloom.jweaver.dictionary.java.TypeWeaverTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

class URLWeaverTest extends TypeWeaverTest {

    URLWeaver weaver = new URLWeaver();

    @Test
    void test() throws MalformedURLException {
        String expected = "URL[https://www.robinloom.com]";

        Assertions.assertEquals(expected, weaver.weave(URL.of(URI.create("https://www.robinloom.com"), null), ctx));
    }
}