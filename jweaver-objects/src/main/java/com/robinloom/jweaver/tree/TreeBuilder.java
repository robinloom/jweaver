package com.robinloom.jweaver.tree;

import com.robinloom.jweaver.util.FieldOperations;
import com.robinloom.jweaver.util.TypeDictionary;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

class TreeBuilder {

    protected static final ThreadLocal<Set<Object>> history
            = ThreadLocal.withInitial(() -> Collections.newSetFromMap(new IdentityHashMap<>()));

    private final TreeConfig config;
    private int depth = 0;

    TreeBuilder(TreeConfig config) {
        this.config = config;
    }

    TreeNode build(TreeNode root, Object object) {
        if (history.get().contains(object)) {
            return root;
        } else {
            history.get().add(object);
        }

        depth++;
        if (depth == config.getMaxDepth()) {
            depth--;
            return root;
        }

        List<Field> fields;
        if (config.isShowInheritedFields()) {
            fields = FieldOperations.getAllFields(object.getClass());
        } else {
            fields = FieldOperations.getFields(object.getClass());
        }

        fields = fields.stream()
                .filter(config::isIncluded)
                .toList();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String dataType = "";
                if (config.isShowDataTypes()) {
                    dataType = "{" + field.getType().getSimpleName() + "} ";
                }

                String fieldName = field.getName();
                if (config.isCapitalizeFields()) {
                    fieldName = FieldOperations.capitalize(fieldName);
                }

                fieldName = dataType + fieldName;

                Object value = field.get(object);
                if (value == null) {
                    continue;
                }

                if (TypeDictionary.isSimpleType(field.getType())) {
                    root.addChild(fieldName, value.toString());
                } else if (TypeDictionary.isCollection(field.getType())) {
                    root.addChild(collection(fieldName, (Collection<?>) value));
                } else if (TypeDictionary.isArray(field.getType())) {
                    root.addChild(array(fieldName, value));
                } else {
                    TreeNode child = new TreeNode(fieldName);
                    root.addChild(build(child, value));
                }
            } catch (InaccessibleObjectException ioe) {
                root.addChild("[?]");
            } catch (Exception e) {
                root.addChild("[ERROR]");
            }
        }

        history.get().clear();

        depth--;
        return root;
    }

    private TreeNode collection(String fieldName, Collection<?> collection) {
        TreeNode root = new TreeNode(fieldName);

        depth++;
        if (depth == config.getMaxDepth()) {
            depth--;
            return root;
        }

        int i = 0;
        for (Object item : collection) {
            String prefix = "(" + i + ") ";

            if (i >= config.getMaxSequenceLength()) {
                root.addChild((collection.size()-i) + " more");
                break;
            } else if (TypeDictionary.isSimpleType(item.getClass())) {
                root.addChild(prefix + item);
            } else if (TypeDictionary.isCollection(item.getClass())) {
                root.addChild(collection(prefix.trim(), (Collection<?>) item));
            } else if (TypeDictionary.isArray(item.getClass())) {
                root.addChild(array(prefix.trim(), item));
            } else {
                TreeNode child = new TreeNode(prefix + item.getClass().getSimpleName());
                root.addChild(build(child, item));
            }
            i++;
        }

        depth--;
        return root;
    }

    private TreeNode array(String fieldName, Object array) {
        TreeNode root = new TreeNode(fieldName);

        depth++;
        if (depth == config.getMaxDepth()) {
            depth--;
            return root;
        }

        int arrayLength = Array.getLength(array);
        for (int i = 0; i < arrayLength; i++) {
            Object item = Array.get(array, i);
            String prefix = "[" + i + "] ";

            if (i >= config.getMaxSequenceLength()) {
                root.addChild((arrayLength-i) + " more");
                break;
            } else if (TypeDictionary.isSimpleType(item.getClass())) {
                root.addChild(prefix + item);
            } else if (TypeDictionary.isCollection(item.getClass())) {
                root.addChild(collection(prefix.trim(), (Collection<?>) item));
            } else if (TypeDictionary.isArray(item.getClass())) {
                root.addChild(array(prefix.trim(), item));
            } else {
                TreeNode child = new TreeNode(prefix + item.getClass().getSimpleName());
                root.addChild(build(child, item));
            }
        }
        depth--;
        return root;
    }
}
