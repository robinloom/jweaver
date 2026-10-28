package com.robinloom.jweaver.ast;

import com.robinloom.jweaver.Mode;
import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.jweaver.ast.nodes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

public class ReflectiveASTTest {

    private WeavingContext dummyContext() {
        return new WeavingContext(
                Mode.TREE,
                _ -> null,
                _ -> (value, _) -> value.toString(),
                true
        );
    }

    @Test
    void testArrayExpansion() {
        int[] array = new int[]{1, 2, 3};

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode result = ast.build(array, dummyContext());

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(SequenceNode.class, result);
        Assertions.assertEquals(3, result.getChildren().size());
    }

    @Test
    void testListExpansion() {
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode result = ast.build(list, dummyContext());

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(SequenceNode.class, result);
        Assertions.assertEquals(3, result.getChildren().size());
    }

    @Test
    void testSimpleMapExpansion() {
        Map<String, String> map = new HashMap<>();
        map.put("one", "one");
        map.put("two", "two");
        map.put("three", "three");

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode result = ast.build(map, dummyContext());

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(SequenceNode.class, result);
        Assertions.assertEquals(3, result.getChildren().size());

        ReflectiveNode child1 = result.getChildren().getFirst();
        Assertions.assertInstanceOf(PropertyNode.class, child1);
        Assertions.assertTrue(child1.getChildren().isEmpty());
    }

    @Test
    void testDomainObjectMapExpansion() {
        record ContextId(int id) {}
        record Person(String name, int age) { }

        Map<ContextId, Person> map = new HashMap<>();
        map.put(new ContextId(1), new Person("one", 12));
        map.put(new ContextId(2), new Person("two", 13));
        map.put(new ContextId(3), new Person("three", 14));

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode result = ast.build(map, dummyContext());

        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(SequenceNode.class, result);
        Assertions.assertEquals(3, result.getChildren().size());

        ReflectiveNode child1 = result.getChildren().getFirst();
        Assertions.assertInstanceOf(MapEntryNode.class, child1);
        Assertions.assertEquals(1, child1.getChildren().size());
    }

    @Test
    void testCycleIsDetectedAndStopped() {
        class Node {
            Node next;
        }

        Node a = new Node();
        Node b = new Node();

        a.next = b;
        b.next = a; // cycle

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(a, dummyContext());

        ReflectiveNode nextNode = root.getChildren().getFirst();
        ReflectiveNode nested = nextNode.getChildren().getFirst();

        Assertions.assertTrue(nested.getChildren().isEmpty(), "Cycle should stop traversal");
    }

    @Test
    void testSelfReference() {
        class Self {
            Self self;
        }

        Self s = new Self();
        s.self = s;

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(s, dummyContext());

        ReflectiveNode child = root.getChildren().getFirst();

        Assertions.assertTrue(child.getChildren().isEmpty(), "Self reference must not recurse infinitely");
    }

    @Test
    void testMaxDepthIsRespected() {
        class Node {
            Node next;
        }

        Node a = new Node();
        Node b = new Node();
        Node c = new Node();

        a.next = b;
        b.next = c;

        ASTOptions options = new ASTOptions(1, 10);
        ReflectiveAST ast = new ReflectiveAST(options);

        ReflectiveNode root = ast.build(a, dummyContext());

        ReflectiveNode firstLevel = root.getChildren().getFirst();

        Assertions.assertTrue(firstLevel.getChildren().isEmpty(), "Depth limit should cut traversal");
    }

    @Test
    void testMaxSequenceLength() {
        List<Integer> list = IntStream.range(0, 10).boxed().toList();

        ASTOptions options = new ASTOptions(10, 3);
        ReflectiveAST ast = new ReflectiveAST(options);

        ReflectiveNode root = ast.build(list, dummyContext());

        Assertions.assertEquals(4, root.getChildren().size(), "Should contain 3 elements + 'more' node");

        ReflectiveNode last = root.getChildren().get(3);
        Assertions.assertTrue(last.toString().contains("more"));
    }

    @Test
    void testMapEntries() {
        Map<String, Integer> map = Map.of("a", 1, "b", 2);

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(map, dummyContext());

        Assertions.assertEquals(2, root.getChildren().size());
    }

    @Test
    void testTraversalStateIsRestoredAfterExceptionDuringSequenceIteration() {
        class BrokenCollection extends AbstractCollection<Object> {
            @Override
            public int size() {
                return 1;
            }

            @Override
            public Iterator<Object> iterator() {
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        // Simulates e.g. a ConcurrentModificationException thrown
                        // while iterating a live, concurrently mutated collection.
                        throw new RuntimeException("boom");
                    }

                    @Override
                    public Object next() {
                        throw new NoSuchElementException();
                    }
                };
            }
        }

        class Sibling {
            @SuppressWarnings("unused")
            final String value = "reachable";
        }

        class Root {
            @SuppressWarnings("unused")
            final Collection<Object> broken = new BrokenCollection();
            @SuppressWarnings("unused")
            final Sibling sibling = new Sibling();
        }

        ASTOptions options = new ASTOptions(2, 10);
        ReflectiveAST ast = new ReflectiveAST(options);

        ReflectiveNode root = ast.build(new Root(), dummyContext());

        ReflectiveNode brokenChild = root.getChildren().get(0);
        Assertions.assertInstanceOf(PropertyNode.class, brokenChild,
                "An exception while iterating a sequence must degrade to a single '[?]' node");

        ReflectiveNode siblingChild = root.getChildren().get(1);
        Assertions.assertFalse(siblingChild.getChildren().isEmpty(),
                "TraversalContext must stay balanced (enter/exit) even when iteration throws, "
                        + "otherwise the depth budget silently leaks and truncates unrelated siblings");
    }

    @Test
    void testRegisteredTypeWeaverTakesPrecedenceOverReflectiveExpansion() {
        class Money {
            @SuppressWarnings("unused")
            final int cents = 100;
        }

        TypeWeaver moneyWeaver = new TypeWeaver() {
            @Override
            public Class<?> targetType() {
                return Money.class;
            }

            @Override
            public String weave(Object object, WeavingContext ctx) {
                return "MONEY";
            }
        };

        WeavingContext ctx = new WeavingContext(
                Mode.TREE,
                clazz -> Money.class.equals(clazz) ? moneyWeaver : null,
                _ -> (value, _) -> value.toString(),
                true
        );

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(new Money(), ctx);

        Assertions.assertInstanceOf(PropertyNode.class, root,
                "A type with a registered TypeWeaver must not be reflectively expanded");
        Assertions.assertEquals("MONEY", ((PropertyNode) root).getValue());
        Assertions.assertTrue(root.getChildren().isEmpty());
    }

    @Test
    void testRegisteredTypeWeaverTakesPrecedenceForMapKeys() {
        class UserId {
            @SuppressWarnings("unused")
            final int value = 42;
        }

        TypeWeaver userIdWeaver = new TypeWeaver() {
            @Override
            public Class<?> targetType() {
                return UserId.class;
            }

            @Override
            public String weave(Object object, WeavingContext ctx) {
                return "USER-42";
            }
        };

        WeavingContext ctx = new WeavingContext(
                Mode.TREE,
                clazz -> UserId.class.equals(clazz) ? userIdWeaver : null,
                _ -> (value, _) -> value.toString(),
                true
        );

        record Profile(String displayName) {}

        Map<UserId, Profile> map = new HashMap<>();
        map.put(new UserId(), new Profile("Jane"));

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(map, ctx);

        ReflectiveNode entry = root.getChildren().getFirst();
        Assertions.assertInstanceOf(MapEntryNode.class, entry);
        Assertions.assertEquals("USER-42", entry.toString(),
                "Map key with a registered TypeWeaver must use it instead of raw toString()");
    }

    @Test
    void testSensitiveFieldIsMasked() {
        class User {
            @SuppressWarnings("unused")
            final String password = "secret";
        }

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(new User(), dummyContext());

        PropertyNode child = (PropertyNode) root.getChildren().getFirst();

        Assertions.assertEquals("***", child.getValue());
    }

    @Test
    void testInheritedFieldsAreRendered() {
        class Base {
            @SuppressWarnings("unused")
            final String id = "abc-123";
        }

        class Child extends Base {
            @SuppressWarnings("unused")
            final String name = "Jane";
        }

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(new Child(), dummyContext());

        List<String> fieldNames = root.getChildren().stream()
                .map(ReflectiveNode::toString)
                .toList();

        Assertions.assertEquals(2, root.getChildren().size(),
                "Fields inherited from a superclass must be part of the object's structure");
        Assertions.assertTrue(fieldNames.stream().anyMatch(s -> s.startsWith("id=")),
                "Inherited field 'id' from Base must be rendered");
        Assertions.assertTrue(fieldNames.stream().anyMatch(s -> s.startsWith("name=")),
                "Own field 'name' from Child must still be rendered");
    }

    @Test
    void testBuildWithNullObjectReturnsRootNullPropertyNode() {
        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(null, dummyContext());

        Assertions.assertInstanceOf(PropertyNode.class, root);
        Assertions.assertEquals("null", ((PropertyNode) root).getValue());
    }

    @Test
    void testBuildDegradesToErrorNodeWhenTraversalThrows() {
        WeavingContext throwingCtx = new WeavingContext(
                Mode.TREE,
                _ -> { throw new RuntimeException("boom"); },
                _ -> (value, _) -> value.toString(),
                true
        );

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(new Object(), throwingCtx);

        Assertions.assertInstanceOf(PropertyNode.class, root);
        Assertions.assertEquals("[error]", ((PropertyNode) root).getValue(),
                "An unexpected exception during traversal must degrade to a single error node");
    }

    @Test
    void testWeaveNameAnnotationOverridesFieldName() {
        class Address {
            @com.robinloom.jweaver.annotation.WeaveName("zip")
            @SuppressWarnings("unused")
            final String postalCode = "12345";
        }

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(new Address(), dummyContext());

        ReflectiveNode child = root.getChildren().getFirst();
        Assertions.assertTrue(child.toString().startsWith("zip="),
                "@WeaveName must override the reflected field name");
    }

    @Test
    void testNullFieldValueBecomesNullPropertyNode() {
        class WithNullField {
            @SuppressWarnings("unused")
            final String name = null;
        }

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(new WithNullField(), dummyContext());

        PropertyNode child = (PropertyNode) root.getChildren().getFirst();
        Assertions.assertEquals("null", child.getValue());
    }

    @Test
    void testNullMapValueBecomesNullPropertyNode() {
        Map<String, String> map = new HashMap<>();
        map.put("key", null);

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(map, dummyContext());

        PropertyNode entry = (PropertyNode) root.getChildren().getFirst();
        Assertions.assertEquals("null", entry.getValue());
    }

    @Test
    void testSelfReferencingCollectionStopsRecursion() {
        List<Object> list = new ArrayList<>();
        list.add(list);

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(list, dummyContext());

        Assertions.assertEquals(1, root.getChildren().size());

        ReflectiveNode child = root.getChildren().getFirst();
        Assertions.assertTrue(child.getChildren().isEmpty(),
                "A collection containing itself must not recurse infinitely");
    }

    @Test
    void testNullElementInSequenceBecomesNullPropertyNode() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(list, dummyContext());

        ReflectiveNode nullChild = root.getChildren().get(1);
        Assertions.assertInstanceOf(PropertyNode.class, nullChild);
        Assertions.assertEquals("null", ((PropertyNode) nullChild).getValue());
        Assertions.assertEquals(1, nullChild.getIndex());
    }

    @Test
    void testNestedSequenceFieldIncludesFieldNameInLabel() {
        class WithListField {
            @SuppressWarnings("unused")
            final List<String> items = List.of("a", "b");
        }

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(new WithListField(), dummyContext());

        ReflectiveNode itemsNode = root.getChildren().getFirst();
        Assertions.assertEquals("items=List[2]", itemsNode.toString(),
                "A non-root sequence must render as 'fieldName=Type[size]'");
    }

    @Test
    void testRegisteredTypeWeaverTakesPrecedenceOverGenericMapRendering() {
        class SpecialMap extends HashMap<String, String> {
            SpecialMap() {
                put("a", "1");
                put("b", "2");
            }
        }

        TypeWeaver specialMapWeaver = new TypeWeaver() {
            @Override
            public Class<?> targetType() {
                return SpecialMap.class;
            }

            @Override
            public String weave(Object object, WeavingContext ctx) {
                return "SPECIAL_MAP";
            }
        };

        WeavingContext ctx = new WeavingContext(
                Mode.TREE,
                clazz -> SpecialMap.class.equals(clazz) ? specialMapWeaver : null,
                _ -> (value, _) -> value.toString(),
                true
        );

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(new SpecialMap(), ctx);

        Assertions.assertInstanceOf(PropertyNode.class, root,
                "A Map-implementing type with a registered TypeWeaver must use it instead of "
                        + "being flattened into its entries (e.g. java.security.Provider)");
        Assertions.assertEquals("SPECIAL_MAP", ((PropertyNode) root).getValue());
    }

}
