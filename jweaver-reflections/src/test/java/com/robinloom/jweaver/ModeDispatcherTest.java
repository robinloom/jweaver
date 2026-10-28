package com.robinloom.jweaver;

import com.robinloom.jweaver.inline.InlineWeaver;
import com.robinloom.jweaver.tree.TreeWeaver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ModeDispatcherTest {

    @Test
    void testGetInstanceReturnsSingleton() {
        Assertions.assertSame(ModeDispatcher.getInstance(), ModeDispatcher.getInstance());
    }

    @Test
    void testTreeModeResolvesToTreeWeaver() {
        Weaver weaver = ModeDispatcher.getInstance().resolve(Mode.TREE);

        Assertions.assertInstanceOf(TreeWeaver.class, weaver);
    }

    @Test
    void testInlineModeResolvesToInlineWeaver() {
        Weaver weaver = ModeDispatcher.getInstance().resolve(Mode.INLINE);

        Assertions.assertInstanceOf(InlineWeaver.class, weaver);
    }

    @Test
    void testNullModeFallsBackToInlineWeaver() {
        Weaver weaver = ModeDispatcher.getInstance().resolve(null);

        Assertions.assertInstanceOf(InlineWeaver.class, weaver);
    }
}
