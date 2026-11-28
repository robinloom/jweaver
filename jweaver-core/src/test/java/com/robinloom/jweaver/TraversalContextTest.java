package com.robinloom.jweaver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TraversalContextTest {

    @Test
    void testSameInstanceCannotBeEnteredTwice() {
        TraversalContext ctx = new TraversalContext(10);
        Object o = new Object();

        Assertions.assertTrue(ctx.enter(o));
        Assertions.assertFalse(ctx.enter(o), "Re-entering the same instance must be rejected to prevent cycles");
    }

    @Test
    void testEqualButDistinctInstancesAreBothAllowed() {
        TraversalContext ctx = new TraversalContext(10);
        String a = new String("same");
        String b = new String("same");

        Assertions.assertTrue(ctx.enter(a));
        Assertions.assertTrue(ctx.enter(b), "Identity, not equality, must be used to detect cycles");
    }

    @Test
    void testExitAllowsReentry() {
        TraversalContext ctx = new TraversalContext(10);
        Object o = new Object();

        Assertions.assertTrue(ctx.enter(o));
        ctx.exit(o);
        Assertions.assertTrue(ctx.enter(o), "After exit(), the object must be visitable again");
    }

    @Test
    void testMaxDepthIsEnforced() {
        TraversalContext ctx = new TraversalContext(2);

        Assertions.assertTrue(ctx.enter(new Object()));
        Assertions.assertTrue(ctx.enter(new Object()));
        Assertions.assertFalse(ctx.enter(new Object()), "Traversal must stop once the configured max depth is reached");
    }

    @Test
    void testIsLimitReachedForDepth() {
        TraversalContext ctx = new TraversalContext(1);

        Assertions.assertFalse(ctx.isLimitReached());
        ctx.enter(new Object());
        Assertions.assertTrue(ctx.isLimitReached());
    }

    @Test
    void testMaxNodesCountsEveryEnterEvenAfterExit() {
        TraversalContext ctx = new TraversalContext(10, 3);

        for (int i = 0; i < 3; i++) {
            Object o = new Object();
            Assertions.assertTrue(ctx.enter(o));
            ctx.exit(o);
        }

        Assertions.assertTrue(ctx.isLimitReached());
        Assertions.assertFalse(ctx.enter(new Object()), "Node limit must hold although the depth is back at 0");
    }

    @Test
    void testResetClearsNodeCount() {
        TraversalContext ctx = new TraversalContext(10, 1);
        Object o = new Object();
        ctx.enter(o);
        ctx.exit(o);

        ctx.reset();

        Assertions.assertTrue(ctx.enter(o));
    }

    @Test
    void testResetClearsVisitedAndDepth() {
        TraversalContext ctx = new TraversalContext(1);
        Object o = new Object();

        Assertions.assertTrue(ctx.enter(o));
        Assertions.assertFalse(ctx.enter(new Object()), "Precondition: depth budget exhausted");

        ctx.reset();

        Assertions.assertTrue(ctx.enter(o), "After reset(), previously visited objects must be visitable again");
    }
}
