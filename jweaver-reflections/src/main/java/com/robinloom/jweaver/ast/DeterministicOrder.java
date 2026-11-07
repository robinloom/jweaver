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
package com.robinloom.jweaver.ast;

import com.robinloom.jweaver.ast.nodes.ReflectiveNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;
import java.util.SequencedMap;
import java.util.Set;
import java.util.function.Function;

/**
 * Brings the items of containers without a defined iteration order into a
 * stable order, so that the rendered output no longer depends on the JVM.
 * <p>
 * Affected are sets and maps such as {@code HashSet}, {@code Set.of},
 * {@code HashMap} and {@code Map.of}. Containers with their own order (lists,
 * {@code LinkedHashMap}, {@code TreeMap}, {@code EnumSet}, ...) are left alone.
 * <p>
 * Items are ordered by their natural order if they are all mutually comparable
 * (e.g. strings, numbers, enums), otherwise by their rendered form.
 */
final class DeterministicOrder {

    /**
     * Upper bound for containers whose items have to be rendered in full to
     * determine their order; larger ones are left unsorted so that
     * {@code maxSequenceLength} keeps protecting against huge graphs.
     */
    private static final int MAX_RENDERED_SORT_SIZE = 1_000;

    private DeterministicOrder() {
    }

    static boolean isUnordered(Collection<?> collection) {
        return collection instanceof Set<?>
                && !(collection instanceof SequencedCollection<?>)
                && !(collection instanceof EnumSet<?>);
    }

    static boolean isUnordered(Map<?, ?> map) {
        return !(map instanceof SequencedMap<?, ?>) && !(map instanceof EnumMap<?, ?>);
    }

    /**
     * Sorts the items in place.
     *
     * @param sortKey extracts the part of an item that determines its position
     *                (the item itself for sets, the key for map entries)
     * @param render  renders an item into a node; only used if the items are
     *                not naturally comparable
     */
    static void sort(List<Object> items, Function<Object, Object> sortKey,
                     Function<Object, ReflectiveNode> render) {
        if (items.size() > 1 && !sortNaturally(items, sortKey) && items.size() <= MAX_RENDERED_SORT_SIZE) {
            sortByRendering(items, render);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static boolean sortNaturally(List<Object> items, Function<Object, Object> sortKey) {
        Class<?> type = null;
        for (Object item : items) {
            Object key = sortKey.apply(item);
            if (key == null) {
                continue;
            }
            if (!(key instanceof Comparable<?>)) {
                return false;
            }
            if (type == null) {
                type = key.getClass();
            } else if (type != key.getClass()) {
                return false;
            }
        }

        try {
            items.sort(Comparator.comparing(sortKey, Comparator.nullsFirst((a, b) -> ((Comparable) a).compareTo(b))));
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private static void sortByRendering(List<Object> items, Function<Object, ReflectiveNode> render) {
        record Keyed(Object item, String key) {}

        List<Keyed> keyed = new ArrayList<>(items.size());
        for (Object item : items) {
            keyed.add(new Keyed(item, item == null ? "" : renderedKey(render.apply(item))));
        }

        keyed.sort(Comparator.comparing(Keyed::key));

        for (int i = 0; i < items.size(); i++) {
            items.set(i, keyed.get(i).item());
        }
    }

    private static String renderedKey(ReflectiveNode node) {
        StringBuilder key = new StringBuilder(node.toString());
        for (ReflectiveNode child : node.getChildren()) {
            key.append('\0').append(renderedKey(child));
        }
        return key.toString();
    }
}
