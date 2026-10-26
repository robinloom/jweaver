/*
 * Copyright (C) 2026 Robin Kösters
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
package com.robinloom.jweaver.dictionary.java.util;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import org.jspecify.annotations.NonNull;

import java.util.TimeZone;

public class TimeZoneWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return TimeZone.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        TimeZone zone = (TimeZone) object;

        return "TimeZone[" + "id=" + zone.getID() + ", " +
                             "offset=" + formatOffset(zone.getRawOffset()) +
                        "]";
    }

    private String formatOffset(int offsetMillis) {
        long totalMinutes = offsetMillis / 60_000;
        String sign = totalMinutes < 0 ? "-" : "+";

        long absMinutes = Math.abs(totalMinutes);
        long hours = absMinutes / 60;
        long minutes = absMinutes % 60;

        return String.format("%s%02d:%02d", sign, hours, minutes);
    }
}
