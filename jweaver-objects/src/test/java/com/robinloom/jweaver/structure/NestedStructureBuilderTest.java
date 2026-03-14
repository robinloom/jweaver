package com.robinloom.jweaver.structure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

class NestedStructureBuilderTest {

    @Test
    void testInaccessibleField() throws ReflectiveOperationException {
        record Person(String name, int age) {}

        NestedStructureBuilder nestedStructureBuilder = Mockito.spy(NestedStructureBuilder.class);

        doThrow(new IllegalAccessException("boom"))
                .when(nestedStructureBuilder)
                .readField(any(), any());

        Person person = new Person("Peter", 38);
        NestedNode node = nestedStructureBuilder.build(new NestedNode(person), person);

        Assertions.assertEquals("Person", node.getContent());
        Assertions.assertEquals("[?]", node.getChildren().getFirst().getContent());
    }
}
