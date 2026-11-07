package com.robinloom.jweaver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class DeterministicOrderTest {

    enum Role { GUEST, ADMIN }

    record Tag(String name, int weight) {}

    static class Plain {
        final String id;

        Plain(String id) {
            this.id = id;
        }
    }

    @Test
    void unorderedSetOfStringsIsSorted() {
        Assertions.assertEquals("Set[4][\"admin\", \"dev\", \"ops\", \"qa\"]",
                JWeaver.weave(Set.of("qa", "ops", "dev", "admin"), Mode.INLINE));
    }

    @Test
    void unorderedSetOfNumbersIsSortedNumerically() {
        Assertions.assertEquals("Set[3][2, 10, 100]",
                JWeaver.weave(new HashSet<>(List.of(100, 2, 10)), Mode.INLINE));
    }

    @Test
    void unorderedSetOfObjectsIsSortedByRendering() {
        Set<Plain> set = new HashSet<>();
        for (String id : List.of("D", "B", "C", "A")) {
            set.add(new Plain(id));
        }

        Assertions.assertEquals("Set[4][Plain[id=\"A\"], Plain[id=\"B\"], Plain[id=\"C\"], Plain[id=\"D\"]]",
                JWeaver.weave(set, Mode.INLINE));
    }

    @Test
    void unorderedSetOfMixedTypesIsSortedByRendering() {
        Set<Object> set = new HashSet<>(List.of("b", 2, "a", 1));

        String first = JWeaver.weave(set, Mode.INLINE);
        Assertions.assertEquals(first, JWeaver.weave(new HashSet<>(List.of(1, "a", 2, "b")), Mode.INLINE));
    }

    @Test
    void unorderedMapIsSortedByKey() {
        Assertions.assertEquals("Map[3][\"a\"=1, \"b\"=2, \"c\"=3]",
                JWeaver.weave(Map.of("c", 3, "a", 1, "b", 2), Mode.INLINE));
    }

    @Test
    void unorderedMapWithRecordKeysIsSortedByRendering() {
        Map<Tag, Integer> map = new HashMap<>();
        map.put(new Tag("z", 1), 1);
        map.put(new Tag("a", 2), 2);

        String output = JWeaver.weave(map, Mode.INLINE);

        Assertions.assertTrue(output.indexOf("name=a") < output.indexOf("name=z"), output);
    }

    @Test
    void treeModeIsSortedAsWell() {
        String expected = """
                Map[2]
                |-- "a"=1
                `-- "b"=2""";

        Assertions.assertEquals(expected, JWeaver.weave(Map.of("b", 2, "a", 1), Mode.TREE));
    }

    @Test
    void truncationKeepsTheSmallestElements() {
        Set<Integer> set = new HashSet<>();
        for (int i = 20; i >= 1; i--) {
            set.add(i);
        }

        String output = JWeaver.weave(set, Mode.INLINE);

        Assertions.assertTrue(output.startsWith("Set[20][1, 2, 3, 4, 5, 6, 7, 8, 9, 10"), output);
    }

    @Test
    void sortedFieldsInsideObjectsAreStable() {
        record Holder(Set<String> names, Map<String, Integer> counts) {}

        Holder holder = new Holder(Set.of("b", "a", "c"), Map.of("y", 1, "x", 2));

        Assertions.assertEquals(
                "Holder[names=Set[3][\"a\", \"b\", \"c\"], counts=Map[2][\"x\"=2, \"y\"=1]]",
                JWeaver.weave(holder, Mode.INLINE));
    }

    @Test
    void orderedContainersKeepTheirOrder() {
        Map<String, Integer> linked = new LinkedHashMap<>();
        linked.put("z", 1);
        linked.put("a", 2);

        Set<String> linkedSet = new LinkedHashSet<>(List.of("z", "a"));

        Assertions.assertEquals("Map[2][\"z\"=1, \"a\"=2]", JWeaver.weave(linked, Mode.INLINE));
        Assertions.assertEquals("Set[2][\"z\", \"a\"]", JWeaver.weave(linkedSet, Mode.INLINE));
        Assertions.assertEquals("List[2][\"z\", \"a\"]", JWeaver.weave(new ArrayList<>(List.of("z", "a")), Mode.INLINE));
        Assertions.assertEquals("String[2][\"z\", \"a\"]", JWeaver.weave(new String[]{"z", "a"}, Mode.INLINE));
    }

    @Test
    void enumSetKeepsOrdinalOrder() {
        Assertions.assertEquals("Set[2][Role.GUEST, Role.ADMIN]",
                JWeaver.weave(EnumSet.of(Role.ADMIN, Role.GUEST), Mode.INLINE));
    }
}
