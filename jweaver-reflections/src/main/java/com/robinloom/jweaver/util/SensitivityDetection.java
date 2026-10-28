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
package com.robinloom.jweaver.util;

import com.robinloom.jweaver.annotation.WeaveRedact;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Heuristic detection of fields that are likely to hold sensitive data and
 * should therefore be masked instead of rendered.
 * <p>
 * A field is considered sensitive if any of the following applies:
 * <ul>
 *     <li>it is explicitly annotated with {@link WeaveRedact}</li>
 *     <li>its declared type is {@code byte[]} or {@code char[]}, the
 *     conventional carriers for raw secrets</li>
 *     <li>its name — split at camelCase/snake_case/kebab-case boundaries —
 *     contains, as a whole word or as an adjacent two-word combination, a
 *     common credential-related term such as {@code password}, {@code secret},
 *     {@code token}, {@code apiKey}, {@code privateKey}, {@code sessionId},
 *     {@code creditCardNumber}, {@code ssn}, or similar</li>
 * </ul>
 * <p>
 * Name matching is case-insensitive and word-based rather than a raw substring
 * check, so that e.g. {@code secretary} or {@code authorId} are not mistaken
 * for {@code secret} or {@code auth}.
 * <p>
 * This classification is heuristic and may evolve as additional patterns
 * are identified.
 */
public final class SensitivityDetection {

    private static final Pattern WORD_BOUNDARY =
            Pattern.compile("(?<=[a-z0-9])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|[_\\-\\s]+");

    private static final Set<String> SENSITIVE_TERMS = Set.of(
            "password", "passwd", "pwd",
            "secret", "token",
            "credential", "credentials",
            "apikey", "privatekey", "accesskey", "secretkey",
            "clientsecret",
            "accesstoken", "refreshtoken", "authtoken", "bearertoken", "sessiontoken",
            "sessionid",
            "ssn", "socialsecuritynumber",
            "creditcard", "cardnumber", "cvv", "cvc",
            "iban", "pin", "pincode"
    );

    private static final Map<Field, Boolean> CACHE = new ConcurrentHashMap<>();

    private SensitivityDetection() {}

    public static boolean isSensitive(Field field) {
        return CACHE.computeIfAbsent(field, SensitivityDetection::computeIsSensitive);
    }

    private static boolean computeIsSensitive(Field field) {
        return isSensitiveByAnnotation(field) || isSensitiveByType(field) || isSensitiveByName(field);
    }

    private static boolean isSensitiveByAnnotation(Field field) {
        return field.isAnnotationPresent(WeaveRedact.class);
    }

    private static boolean isSensitiveByType(Field field) {
        Class<?> type = field.getType();
        return type == char[].class || type == byte[].class;
    }

    private static boolean isSensitiveByName(Field field) {
        List<String> words = splitIntoWords(field.getName());

        for (int i = 0; i < words.size(); i++) {
            if (SENSITIVE_TERMS.contains(words.get(i))) {
                return true;
            }
            if (i + 1 < words.size() && SENSITIVE_TERMS.contains(words.get(i) + words.get(i + 1))) {
                return true;
            }
        }

        return false;
    }

    private static List<String> splitIntoWords(String name) {
        return Arrays.stream(WORD_BOUNDARY.split(name))
                .filter(word -> !word.isEmpty())
                .map(word -> word.toLowerCase(Locale.ROOT))
                .toList();
    }
}
