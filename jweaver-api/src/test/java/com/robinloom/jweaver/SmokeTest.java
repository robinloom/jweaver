package com.robinloom.jweaver;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class SmokeTest {

    @ParameterizedTest
    @MethodSource("forSmokeTest")
    void inlineSmokeTest(Object input) {
        printStandardToString(input);
        printJWeaver(input, Mode.INLINE);
    }

    @ParameterizedTest
    @MethodSource("forSmokeTest")
    void treeSmokeTest(Object input) {
        printStandardToString(input);
        printJWeaver(input, Mode.TREE);
    }

    static Stream<Object> forSmokeTest() {
        // --- Records ---
        record Simple(String name, int age) {}
        record Nested(String name, Simple child) {}
        record Person(String name, List<Person> friends) {}
        record WithArray(String name, String[] tags) {}
        record Monster(Object value) {}

        // --- Self reference ---
        class Self {
            Self self;
        }
        Self self = new Self();
        self.self = self;

        // --- Cyclic ---
        A a = new A();
        B b = new B();
        a.b = b;
        b.a = a;

        // --- Mixed complex ---
        Person complexPerson = new Person(
                "Alice",
                List.of(
                        new Person("Bob", List.of()),
                        new Person("Charlie", List.of())
                )
        );

        return Stream.of(

                // ===== PRIMITIVES & WRAPPERS =====
                "Hello, world",
                "",
                'C',
                1,
                0,
                -1,
                Integer.MAX_VALUE,
                Long.MIN_VALUE,
                2.0,
                -3.14,
                true,
                false,

                // ===== NULL & OPTIONAL =====
                null,
                Optional.empty(),
                Optional.of("value"),

                // ===== ARRAYS =====
                new int[]{},
                new int[]{1, 2, 3},
                new String[]{"a", "b"},
                new Object[]{1, "two", 3.0},
                new String[][]{{"a", "b"}, {"c"}},

                // ===== COLLECTIONS =====
                List.of(),
                List.of("a", "b", "c"),
                new ArrayList<>(List.of("x", "y")),
                new LinkedList<>(List.of(1, 2)),
                Set.of(1, 2, 3),
                new HashSet<>(List.of("a", "b")),
                Map.of("key", "value"),
                Map.of("a", 1, "b", 2),

                // ===== Time =====
                new Date(),
                LocalDate.now(),
                Duration.of(10, ChronoUnit.HOURS),
                ZonedDateTime.now(),

                // ===== LARGE COLLECTION =====
                IntStream.range(0, 50).boxed().toList(),

                // ===== RECORDS =====
                new Simple("Alice", 30),
                new Nested("Parent", new Simple("Child", 10)),

                // ===== RECORD WITH ARRAY =====
                new WithArray("Test", new String[]{"x", "y", "z"}),

                // ===== NESTED STRUCTURES =====
                List.of(List.of(1, 2), List.of(3, 4)),
                Map.of("list", List.of("a", "b")),
                List.of(Map.of("key", List.of(1, 2, 3))),

                // ===== COMPLEX OBJECT =====
                complexPerson,

                // ===== ARRAY OF OBJECTS =====
                new Simple[]{
                        new Simple("A", 1),
                        new Simple("B", 2)
                },

                // ===== SELF & CYCLES =====
                self,
                a,

                // ===== JAVA INTERNALS =====
                new Exception("Test"),
                new RuntimeException("Boom"),
                Thread.currentThread(),
                new Object(),

                // ===== CLASS OBJECTS =====
                String.class,
                int.class,

                // ===== ANONYMOUS CLASS =====
                new Object() {

                    @SuppressWarnings("unused") final int x = 42;
                    @SuppressWarnings("unused") final String y = "test";
                },

                // ===== MONSTER (MIXED) =====
                new Monster(List.of(
                        Map.of("a", new int[]{1, 2, 3}),
                        "string",
                        42,
                        List.of(List.of("deep", List.of(1, 2)))
                ))
        );
    }

    static class A { B b; }
    static class B { A a; }

    private void printStandardToString(Object input) {
        System.out.println("================");
        System.out.println("=== STANDARD ===");
        System.out.println(input == null ? "null" : input.toString());
        System.out.println();
    }

    private void printJWeaver(Object input, Mode mode) {
        System.out.println("=== JWEAVER ===");
        System.out.println(JWeaver.weave(input, mode));
        System.out.println("===============");
    }
}
