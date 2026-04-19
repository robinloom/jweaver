package com.robinloom.jweaver;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public final class TraversalContext {

    private final Set<Object> visited = Collections.newSetFromMap(new IdentityHashMap<>());

    private final int maxDepth;
    private int depth = 0;

    public TraversalContext(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public boolean enter(Object o) {
        if (depth >= maxDepth) {
            return false;
        }

        if (!visited.add(o)) {
            return false;
        }

        depth++;
        return true;
    }

    public void exit() {
        depth--;
    }

    public void reset() {
        visited.clear();
        depth = 0;
    }
}
