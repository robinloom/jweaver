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
package com.robinloom.jweaver.dictionary.java.io;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import org.jspecify.annotations.NonNull;

import java.io.File;

public class FileWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return java.io.File.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        File file = (File) object;

        StringBuilder sb = new StringBuilder("File[");
        sb.append(file.getAbsolutePath());

        if (file.exists()) {
            sb.append(", exists");
        } else {
            sb.append(", missing");
        }

        if (file.isDirectory()) {
            sb.append(", dir");
        }

        if (file.isFile()) {
            sb.append(", file, ");
            sb.append(formatSize(file.length()));
        }

        sb.append("]");
        return sb.toString();
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        return (bytes / (1024 * 1024)) + " MB";
    }
}
