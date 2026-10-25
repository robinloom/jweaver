package com.robinloom.jweaver.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class FieldExtractorTest {

    private final FieldExtractor extractor = new FieldExtractor();

    static class Base {
        String id = "abc-123";
    }

    static class Child extends Base {
        String name = "Jane";
    }

    static class ShadowingChild extends Base {
        String id = "shadowed";
    }

    static class StaticBase {
        static String CONST = "x";
        String id = "abc-123";
    }

    static class StaticChild extends StaticBase {
        String name = "Jane";
    }

    static class ExecutorServiceBase {
        String internalField = "noise";
    }

    static class UserClass extends ExecutorServiceBase {
        String ownField = "value";
    }

    @Test
    void testInheritedFieldsAreIncluded() {
        Set<String> names = extractor.extract(Child.class).stream()
                .map(Field::getName)
                .collect(Collectors.toSet());

        Assertions.assertEquals(Set.of("name", "id"), names);
    }

    @Test
    void testShadowedFieldKeepsSubclassVersion() {
        List<Field> fields = extractor.extract(ShadowingChild.class);
        List<Field> idFields = fields.stream().filter(f -> f.getName().equals("id")).toList();

        Assertions.assertEquals(1, idFields.size());
        Assertions.assertEquals(ShadowingChild.class, idFields.getFirst().getDeclaringClass());
    }

    @Test
    void testStaticSuperclassFieldsAreStillFiltered() {
        Set<String> names = extractor.extract(StaticChild.class).stream()
                .map(Field::getName)
                .collect(Collectors.toSet());

        Assertions.assertEquals(Set.of("id", "name"), names);
    }

    @Test
    void testInfrastructureSuperclassStopsTheWalk() {
        Set<String> names = extractor.extract(UserClass.class).stream()
                .map(Field::getName)
                .collect(Collectors.toSet());

        Assertions.assertEquals(Set.of("ownField"), names);
    }
}
