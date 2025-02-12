package com.robinloom.jweaver.bullet;

import com.robinloom.jweaver.commons.WeavingMachine;

final class BulletWeavingMachine extends WeavingMachine {

    private final BulletConfig config;

    BulletWeavingMachine(BulletConfig config) {
        this.config = config;
    }

    void indent(int level) {
        delegate.append(" ".repeat(config.getIndentation() * level - 1));
        if (level == 1) {
            delegate.append(config.getFirstLevelBulletChar());
            space();
        } else if (level == 2) {
            delegate.append(config.getSecondLevelBulletChar());
            space();
        } else if (level > 2) {
            delegate.append(config.getDeeperLevelBulletChar());
            space();
        }
    }

    void append(String string) {
        delegate.append(string);
    }

    boolean globalLimitReached() {
        return delegate.length() >= config.getGlobalLengthLimit();
    }
}
