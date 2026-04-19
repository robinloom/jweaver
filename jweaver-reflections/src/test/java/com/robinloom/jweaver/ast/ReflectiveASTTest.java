package com.robinloom.jweaver.ast;

import com.robinloom.jweaver.Mode;
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
    void testSharedReferenceWithinSameGraph() {
        class Node {
            Node left;
            Node right;
        }

        Node shared = new Node();

        Node root = new Node();
        root.left = shared;
        root.right = shared;

        ReflectiveAST ast = new ReflectiveAST();

        ReflectiveNode result = ast.build(root, dummyContext());

        ReflectiveNode left = result.getChildren().get(0);
        ReflectiveNode right = result.getChildren().get(1);

        Assertions.assertFalse(left.getChildren().isEmpty());
        Assertions.assertTrue(right.getChildren().isEmpty());
    }

    @Test
    void testMapEntries() {
        Map<String, Integer> map = Map.of("a", 1, "b", 2);

        ReflectiveAST ast = new ReflectiveAST();
        ReflectiveNode root = ast.build(map, dummyContext());

        Assertions.assertEquals(2, root.getChildren().size());
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

}
