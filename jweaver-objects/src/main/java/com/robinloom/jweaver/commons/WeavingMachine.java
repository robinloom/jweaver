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
package com.robinloom.jweaver.commons;

public abstract class WeavingMachine {

    protected final StringBuilder delegate;

    public WeavingMachine() {
        delegate = new StringBuilder();
    }

    public void newline() {
        delegate.append("\n");
    }

    public void space() {
        delegate.append(" ");
    }

    public void removeLastNewline() {
        if (delegate.toString().endsWith("\n")) {
            delegate.deleteCharAt(delegate.length() - 1);
        }
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    public void reset() {
        delegate.setLength(0);
    }
}
