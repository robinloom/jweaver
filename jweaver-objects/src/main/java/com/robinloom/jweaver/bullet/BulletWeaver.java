package com.robinloom.jweaver.bullet;

import com.robinloom.jweaver.structure.NestedNode;
import com.robinloom.jweaver.structure.NestedStructureBuilder;
import com.robinloom.jweaver.util.TypeDictionary;

import java.util.List;

public class BulletWeaver {

    private final BulletConfig config;
    private final NestedStructureBuilder nestedStructureBuilder;
    private final BulletWeavingMachine machine;

    public BulletWeaver() {
        config = new BulletConfig();
        nestedStructureBuilder = new NestedStructureBuilder(config);
        machine = new BulletWeavingMachine(config);
    }

    public BulletWeaver firstLevelBulletChar(char character) {
        config.setFirstLevelBulletChar(character);
        return this;
    }

    public BulletWeaver secondLevelBulletChar(char character) {
        config.setSecondLevelBulletChar(character);
        return this;
    }

    public BulletWeaver deeperLevelBulletChar(char character) {
        config.setDeeperLevelBulletChar(character);
        return this;
    }

    public BulletWeaver indentation(int indentation) {
        config.setIndentation(indentation);
        return this;
    }

    public BulletWeaver maxDepth(int maxDepth) {
        config.setMaxDepth(maxDepth);
        return this;
    }

    public BulletWeaver maxSequenceLength(int maxSequenceLength) {
        config.setMaxSequenceLength(maxSequenceLength);
        return this;
    }

    public BulletWeaver includeFields(List<String> fields) {
        config.setIncludedFields(fields);
        config.setExcludedFields(List.of());
        return this;
    }

    public BulletWeaver excludeFields(List<String> fields) {
        config.setExcludedFields(fields);
        config.setIncludedFields(List.of());
        return this;
    }

    public BulletWeaver capitalizeFields() {
        config.setCapitalizeFields(true);
        return this;
    }

    public BulletWeaver showDataTypes() {
        config.setShowDataTypes(true);
        return this;
    }

    public BulletWeaver showInheritedFields() {
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

        NestedNode structure = nestedStructureBuilder.build(new NestedNode(object), object);
        traverseDepthFirst(structure);

        return machine.toString();
    }

    private void traverseDepthFirst(NestedNode node) {
        if (machine.globalLimitReached()) {
            return;
        }
        if (node.isRoot()) {
            machine.append(node.getContent());
        } else {
            machine.newline();
            machine.indent(node.getLevel());
            machine.append(node.getContent());
        }

        for (NestedNode child : node.getChildren()) {
            traverseDepthFirst(child);
        }
    }

}
