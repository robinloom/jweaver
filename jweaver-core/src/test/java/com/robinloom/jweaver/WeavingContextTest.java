package com.robinloom.jweaver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WeavingContextTest {

    @Test
    void testNullValueIsRenderedAsNullLiteral() {
        WeavingContext ctx = new WeavingContext(Mode.INLINE, _ -> null, _ -> (value, _) -> value.toString(), true);

        Assertions.assertEquals("null", ctx.weave(null));
    }

    @Test
    void testRegisteredTypeWeaverTakesPrecedenceOverReflectiveWeaver() {
        TypeWeaver moneyWeaver = new TypeWeaver() {
            @Override
            public Class<?> targetType() {
                return Integer.class;
            }

            @Override
            public String weave(Object object, WeavingContext ctx) {
                return "MONEY:" + object;
            }
        };

        WeavingContext ctx = new WeavingContext(
                Mode.INLINE,
                clazz -> Integer.class.equals(clazz) ? moneyWeaver : null,
                _ -> (value, _) -> "FALLBACK",
                true
        );

        Assertions.assertEquals("MONEY:42", ctx.weave(42));
    }

    @Test
    void testFallsBackToReflectiveWeaverWhenNoTypeWeaverIsRegistered() {
        WeavingContext ctx = new WeavingContext(
                Mode.INLINE,
                _ -> null,
                _ -> (value, _) -> "FALLBACK:" + value,
                true
        );

        Assertions.assertEquals("FALLBACK:42", ctx.weave(42));
    }

    @Test
    void testNestedWeaveReceivesNonRootChildContext() {
        WeavingContext ctx = new WeavingContext(
                Mode.INLINE,
                _ -> null,
                _ -> (value, nestedCtx) -> "root=" + nestedCtx.isRoot(),
                true
        );

        Assertions.assertTrue(ctx.isRoot());
        Assertions.assertEquals("root=false", ctx.weave(42),
                "Nested weaving must receive a derived, non-root context");
    }

    @Test
    void testWeaveIfKnownTypeReturnsNullWhenNoTypeWeaverRegistered() {
        WeavingContext ctx = new WeavingContext(Mode.INLINE, _ -> null, _ -> (value, _) -> value.toString(), true);

        Assertions.assertNull(ctx.weaveIfKnownType(42));
    }

    @Test
    void testWeaveIfKnownTypeUsesRegisteredTypeWeaver() {
        TypeWeaver weaver = new TypeWeaver() {
            @Override
            public Class<?> targetType() {
                return Integer.class;
            }

            @Override
            public String weave(Object object, WeavingContext ctx) {
                return "KNOWN:" + object;
            }
        };

        WeavingContext ctx = new WeavingContext(
                Mode.INLINE,
                clazz -> Integer.class.equals(clazz) ? weaver : null,
                _ -> (value, _) -> value.toString(),
                true
        );

        Assertions.assertEquals("KNOWN:42", ctx.weaveIfKnownType(42));
    }
}
