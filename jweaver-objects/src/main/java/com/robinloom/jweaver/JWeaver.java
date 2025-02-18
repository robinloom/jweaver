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
package com.robinloom.jweaver;

import com.robinloom.jweaver.bullet.BulletWeaver;
import com.robinloom.jweaver.dynamic.DynamicWeaver;
import com.robinloom.jweaver.tree.TreeWeaver;

/**
 * Base class of the project. Provides static getters for all the weaver types.
 */
public final class JWeaver {

    private JWeaver() {}

    /**
     * Creates and returns a new instance of the default weaver type.
     * Can be used as an entry point for chaining, such as:
     * {@code JWeaver.getDefault().weave(object);}
     * @return a new DynamicWeaver instance
     */
    public static DynamicWeaver getDefault() {
        return new DynamicWeaver();
    }

    /**
     * Creates and returns a new instance of {@link DynamicWeaver}.
     * Can be used as an entry point for chaining, such as
     * {@code JWeaver.getDynamic().weave(object);}
     * @return a new DynamicWeaver instance
     */
    public static DynamicWeaver getDynamic() {
        return new DynamicWeaver();
    }

    /**
     * Creates and returns a new instance of {@link TreeWeaver}.
     * Can be used as an entry point for chaining, such as
     * {@code JWeaver.getTree().weave(object);}
     * @return a new TreeWeaver instance
     */
    public static TreeWeaver getTree() {
        return new TreeWeaver();
    }

    /**
     * Creates and returns a new instance of {@link BulletWeaver}.
     * Can be used as an entry point for chaining, such as
     * {@code JWeaver.getBullet().weave(object);}
     * @return a new BulletWeaver instance
     */
    public static BulletWeaver getBullet() {
        return new BulletWeaver();
    }

}
