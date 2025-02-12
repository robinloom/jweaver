package com.robinloom.jweaver.bullet;

import com.robinloom.jweaver.commons.WeaverConfig;

public class BulletConfig extends WeaverConfig {

    private char firstLevelBulletChar = '-';
    private char secondLevelBulletChar = '-';
    private char deeperLevelBulletChar = '-';
    private int indentation = 2;

    public char getFirstLevelBulletChar() {
        return firstLevelBulletChar;
    }

    public void setFirstLevelBulletChar(char firstLevelBulletChar) {
        this.firstLevelBulletChar = firstLevelBulletChar;
    }

    public char getSecondLevelBulletChar() {
        return secondLevelBulletChar;
    }

    public void setSecondLevelBulletChar(char secondLevelBulletChar) {
        this.secondLevelBulletChar = secondLevelBulletChar;
    }

    public char getDeeperLevelBulletChar() {
        return deeperLevelBulletChar;
    }

    public void setDeeperLevelBulletChar(char deeperLevelBulletChar) {
        this.deeperLevelBulletChar = deeperLevelBulletChar;
    }

    public int getIndentation() {
        return indentation;
    }

    public void setIndentation(int indentation) {
        this.indentation = indentation;
    }
}
