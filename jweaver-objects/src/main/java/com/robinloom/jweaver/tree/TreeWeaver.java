package com.robinloom.jweaver.tree;

import com.robinloom.jweaver.structure.NestedStructureBuilder;
import com.robinloom.jweaver.structure.NestedNode;
import com.robinloom.jweaver.util.TypeDictionary;

import java.util.ArrayList;
import java.util.List;

public class TreeWeaver {
    
    private final TreeConfig config;
    private final NestedStructureBuilder nestedStructureBuilder;
    private final TreeWeavingMachine machine;

    public TreeWeaver() {
        config = new TreeConfig();
        nestedStructureBuilder = new NestedStructureBuilder(config);
        machine = new TreeWeavingMachine(config);
    }

    public TreeWeaver maxDepth(int maxDepth) {
        config.setMaxDepth(maxDepth);
        return this;
    }

    public TreeWeaver maxSequenceLength(int maxSequenceLength) {
        config.setMaxSequenceLength(maxSequenceLength);
        return this;
    }

    public TreeWeaver branchChar(char branchChar) {
        config.setBranchChar(branchChar);
        return this;
    }

    public TreeWeaver lastBranchChar(char lastBranchChar) {
        config.setLastBranchChar(lastBranchChar);
        return this;
    }

    public TreeWeaver includeFields(List<String> fields) {
        config.setIncludedFields(fields);
        config.setExcludedFields(List.of());
        return this;
    }

    public TreeWeaver excludeFields(List<String> fields) {
        config.setExcludedFields(fields);
        config.setIncludedFields(List.of());
        return this;
    }

    public TreeWeaver capitalizeFields() {
        config.setCapitalizeFields(true);
        return this;
    }

    public TreeWeaver showDataTypes() {
        config.setShowDataTypes(true);
        return this;
    }

    public TreeWeaver showInheritedFields() {
        config.setShowInheritedFields(true);
        return this;
    }

    public String weave(Object object) {
        if (object == null) {
            return "null";
        }
        if (TypeDictionary.isJdkType(object.getClass())) {
            return object.toString();
        }

        NestedNode tree = nestedStructureBuilder.build(new NestedNode(object), object);

        List<Boolean> siblingsAtCurrentLevel = new ArrayList<>();
        traverseDepthFirst(tree, siblingsAtCurrentLevel);

        return machine.toString();
    }

    private void traverseDepthFirst(NestedNode node, List<Boolean> siblingsAtCurrentLevel) {
        if (machine.globalLimitReached()) {
            return;
        }
        if (node.isRoot()) {
            machine.append(node.getContent());
        } else {
            machine.newline();

            for (int i = 0; i < siblingsAtCurrentLevel.size() - 1; i++) {
                if (siblingsAtCurrentLevel.get(i)) {
                    machine.indentWithCrossingBranch();
                } else {
                    machine.indent();
                }
            }

            if (node.isLastChild()) {
                machine.appendLastBranch();
            } else {
                machine.appendBranch();
            }

            machine.append(node.getContent());
        }

        List<NestedNode> children = node.getChildren();

        for (int i = 0; i < children.size(); i++) {
            NestedNode child = children.get(i);

            List<Boolean> siblingsAtNextLevel = new ArrayList<>(siblingsAtCurrentLevel);

            siblingsAtNextLevel.add(i < children.size() - 1);
            traverseDepthFirst(child, siblingsAtNextLevel);
        }
    }
}
