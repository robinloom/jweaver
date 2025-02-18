/*
 * Copyright (C) 2025 Robin Kösters
 * mail[at]robinloom[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.robinloom.jweaver.bullet;

import com.robinloom.jweaver.commons.Properties;
import com.robinloom.jweaver.commons.WeaverConfig;

public class BulletConfig extends WeaverConfig {

    private char firstLevelBulletChar;
    private char secondLevelBulletChar;
    private char deeperLevelBulletChar;
    private int indentation;

    public BulletConfig() {
        firstLevelBulletChar = Properties.BULLET_FIRST_LEVEL_CHAR.getChar('-');
        secondLevelBulletChar = Properties.BULLET_SECOND_LEVEL_CHAR.getChar('-');
        deeperLevelBulletChar = Properties.BULLET_DEEPER_LEVEL_CHAR.getChar('-');
        indentation = Properties.BULLET_INDENTATION.getInt(2);
    }

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
