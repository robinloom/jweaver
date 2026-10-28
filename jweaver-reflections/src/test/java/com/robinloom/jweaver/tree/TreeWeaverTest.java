package com.robinloom.jweaver.tree;

import com.robinloom.jweaver.Mode;
import com.robinloom.jweaver.WeavingContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class TreeWeaverTest {

    private WeavingContext dummyContext() {
        return new WeavingContext(
                Mode.TREE,
                _ -> null,
                _ -> (value, _) -> value.toString(),
                true
        );
    }

    @Test
    void testTreeStructureUsesBranchAndContinuationMarkers() {
        class Leaf {
            @SuppressWarnings("unused")
            final int value = 5;
        }

        class Branch {
            @SuppressWarnings("unused")
            final int id = 1;
            @SuppressWarnings("unused")
            final Leaf leaf = new Leaf();
            @SuppressWarnings("unused")
            final int tag = 9;
        }

        TreeWeaver weaver = new TreeWeaver();
        String result = weaver.weave(new Branch(), dummyContext());

        Assertions.assertEquals(
                """
                        Branch
                        |-- id=1
                        |-- leaf=Leaf
                        |   `-- value=5
                        `-- tag=9""",
                result
        );
    }

    @Test
    void testSequenceElementsShowIndexPrefix() {
        List<String> list = List.of("a", "b");

        TreeWeaver weaver = new TreeWeaver();
        String result = weaver.weave(list, dummyContext());

        Assertions.assertEquals(
                """
                        List[2]
                        |-- [0] a
                        `-- [1] b""",
                result
        );
    }

    @Test
    void testContinuationPrefixAcrossMultipleAncestorLevels() {
        class Deep {
            @SuppressWarnings("unused")
            final int v = 1;
        }

        class LeafA {
            @SuppressWarnings("unused")
            final Deep deep = new Deep();
        }

        class Branch1 {
            @SuppressWarnings("unused")
            final LeafA leafA = new LeafA();
        }

        class Root {
            @SuppressWarnings("unused")
            final Branch1 branch1 = new Branch1();
            @SuppressWarnings("unused")
            final int branch2 = 2;
        }

        TreeWeaver weaver = new TreeWeaver();
        String result = weaver.weave(new Root(), dummyContext());

        Assertions.assertEquals(
                """
                        Root
                        |-- branch1=Branch1
                        |   `-- leafA=LeafA
                        |       `-- deep=Deep
                        |           `-- v=1
                        `-- branch2=2""",
                result,
                "Prefix must combine '|   ' and '    ' correctly across multiple ancestor levels"
        );
    }
}
