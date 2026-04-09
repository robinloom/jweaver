package com.robinloom.jweaver;

import com.robinloom.jweaver.bullet.BulletWeaver;
import com.robinloom.jweaver.card.CardWeaver;
import com.robinloom.jweaver.inline.InlineWeaver;
import com.robinloom.jweaver.tree.TreeWeaver;

public class ModeDispatcher implements ReflectionWeaverResolver {

    private static class Holder {
        private static final ModeDispatcher INSTANCE = new ModeDispatcher();
    }

    public static ModeDispatcher getInstance() {
        return Holder.INSTANCE;
    }

    private ModeDispatcher() {}

    /**
     * Returns a new {@link Weaver} instance suitable for the given mode.
     * <p>
     * The returned weaver is stateless and may be instantiated per call.
     *
     * @param mode the mode to resolve a weaver for
     * @return a corresponding {@link Weaver} implementation
     */
    @Override
    public Weaver resolve(Mode mode) {
        return switch (mode) {
            case TREE -> new TreeWeaver();
            case BULLET -> new BulletWeaver();
            case CARD -> new CardWeaver();
            case null, default -> new InlineWeaver();
        };
    }
}
