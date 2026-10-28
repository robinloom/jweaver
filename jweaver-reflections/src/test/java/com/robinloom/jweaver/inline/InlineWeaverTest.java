package com.robinloom.jweaver.inline;

import com.robinloom.jweaver.Mode;
import com.robinloom.jweaver.WeavingContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class InlineWeaverTest {

    private WeavingContext dummyContext() {
        return new WeavingContext(
                Mode.INLINE,
                _ -> null,
                _ -> (value, _) -> value.toString(),
                true
        );
    }

    @Test
    void testFieldsAreCommaSeparatedAndBracketed() {
        class Leaf {
            @SuppressWarnings("unused")
            final int value = 5;
        }

        class Branch {
            @SuppressWarnings("unused")
            final int id = 1;
            @SuppressWarnings("unused")
            final Leaf leaf = new Leaf();
            @SuppressWarnings("unused")
            final int tag = 9;
        }

        InlineWeaver weaver = new InlineWeaver();
        String result = weaver.weave(new Branch(), dummyContext());

        Assertions.assertEquals("Branch[id=1, leaf=Leaf[value=5], tag=9]", result);
    }

    @Test
    void testMapEntryUsesEqualsSeparatorInsteadOfBrackets() {
        Map<String, Integer> map = Map.of("a", 1);

        InlineWeaver weaver = new InlineWeaver();
        String result = weaver.weave(map, dummyContext());

        Assertions.assertEquals("Map[1][a=1]", result);
    }

    @Test
    void testMapEntryWithComplexValueUsesEqualsSeparator() {
        class Address {
            @SuppressWarnings("unused")
            final String city = "Berlin";
        }

        Map<String, Address> map = new java.util.LinkedHashMap<>();
        map.put("home", new Address());

        InlineWeaver weaver = new InlineWeaver();
        String result = weaver.weave(map, dummyContext());

        Assertions.assertEquals("Map[1][home=Address[city=Berlin]]", result,
                "A map entry with an expandable value must still use '=' instead of being bracketed like an object");
    }

    @Test
    void testBareScalarRootHasNoTrailingComma() {
        InlineWeaver weaver = new InlineWeaver();
        String result = weaver.weave(42, dummyContext());

        Assertions.assertEquals("Integer=42", result);
    }
}
