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
package com.robinloom.jweaver.logging;

import com.robinloom.jweaver.JWeaver;
import com.robinloom.jweaver.Mode;
import com.robinloom.jweaver.commons.Weaver;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.Arrays;

/**
 * A wrapper class for {@link Logger}.
 * When formatting Strings, it will use the provided {@link Weaver}
 * to transform given object arguments into a pretty String representation.
 */
public class JWeaverLogger implements Logger {

    private final Logger logger;
    private final Mode mode;

    public JWeaverLogger(Logger logger, Mode mode) {
        this.logger = logger;
        this.mode = mode;
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        logger.trace(format, JWeaver.weave(arg, mode));
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        logger.trace(format, JWeaver.weave(arg1, mode), JWeaver.weave(arg2, mode));
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.trace(format, Arrays.stream(arguments).map(a -> JWeaver.weave(a, mode)).toArray());
    }

    @Override
    public void trace(String msg, Throwable t) {
        logger.trace(msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        logger.trace(marker, msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        logger.trace(marker, format, JWeaver.weave(arg, mode));
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        logger.trace(marker, format, JWeaver.weave(arg1, mode), JWeaver.weave(arg2, mode));
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        logger.trace(marker, format, Arrays.stream(argArray).map(a -> JWeaver.weave(a, mode)).toArray());
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        logger.trace(marker, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        logger.debug(format, JWeaver.weave(arg, mode));
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(format, JWeaver.weave(arg1, mode), JWeaver.weave(arg2, mode));
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.debug(format, Arrays.stream(arguments).map(a -> JWeaver.weave(a, mode)).toArray());
    }

    @Override
    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        logger.debug(marker, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        logger.debug(marker, format, JWeaver.weave(arg, mode));
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        logger.debug(marker, format, JWeaver.weave(arg1, mode), JWeaver.weave(arg2, mode));
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        logger.debug(marker, format, Arrays.stream(arguments).map(a -> JWeaver.weave(a, mode)).toArray());
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        logger.debug(marker, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        logger.info(format, JWeaver.weave(arg, mode));
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        logger.info(format, JWeaver.weave(arg1, mode), JWeaver.weave(arg2, mode));
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.info(format, Arrays.stream(arguments).map(a -> JWeaver.weave(a, mode)).toArray());
    }

    @Override
    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        logger.info(marker, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        logger.info(marker, format, JWeaver.weave(arg, mode));
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        logger.info(marker, format, JWeaver.weave(arg1, mode), JWeaver.weave(arg2, mode));
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        logger.info(marker, format, Arrays.stream(arguments).map(a -> JWeaver.weave(a, mode)).toArray());
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        logger.info(marker, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        logger.warn(format, JWeaver.weave(arg, mode));
    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.warn(format, Arrays.stream(arguments).map(a -> JWeaver.weave(a, mode)).toArray());
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        logger.warn(format, JWeaver.weave(arg1, mode), JWeaver.weave(arg2, mode));
    }

    @Override
    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        logger.warn(marker, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        logger.warn(marker, format, JWeaver.weave(arg, mode));
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        logger.warn(marker, format, JWeaver.weave(arg1, mode), JWeaver.weave(arg2, mode));
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        logger.warn(marker, format, Arrays.stream(arguments).map(a -> JWeaver.weave(a, mode)).toArray());
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        logger.warn(marker, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        logger.error(format, JWeaver.weave(arg, mode));
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        logger.error(format, JWeaver.weave(arg1, mode), JWeaver.weave(arg2, mode));
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(format, Arrays.stream(arguments).map(a -> JWeaver.weave(a, mode)).toArray());
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        logger.error(marker, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        logger.error(marker, format, JWeaver.weave(arg, mode));
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        logger.error(marker, format, JWeaver.weave(arg1, mode), JWeaver.weave(arg2, mode));
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        logger.error(marker, format, Arrays.stream(arguments).map(a -> JWeaver.weave(a, mode)).toArray());
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        logger.error(marker, msg, t);
    }
}
