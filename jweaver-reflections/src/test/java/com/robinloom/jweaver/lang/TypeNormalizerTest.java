package com.robinloom.jweaver.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class TypeNormalizerTest {

    @Test
    void testNullTypeNormalizesToObject() {
        Assertions.assertEquals(Object.class, TypeNormalizer.normalize(null));
    }

    @Test
    void testPlainTypeIsReturnedUnchanged() {
        Assertions.assertEquals(String.class, TypeNormalizer.normalize(String.class));
    }

    @Test
    void testSetImplementationsNormalizeToSetInterface() {
        Assertions.assertEquals(Set.class, TypeNormalizer.normalize(HashSet.class));
    }

    @Test
    void testListImplementationsNormalizeToListInterface() {
        Assertions.assertEquals(List.class, TypeNormalizer.normalize(List.of().getClass()));
    }

    @Test
    void testOtherCollectionsNormalizeToCollectionInterface() {
        class PlainCollection extends java.util.AbstractCollection<Object> {
            @Override
            public java.util.Iterator<Object> iterator() {
                return java.util.Collections.emptyIterator();
            }

            @Override
            public int size() {
                return 0;
            }
        }

        Assertions.assertEquals(Collection.class, TypeNormalizer.normalize(PlainCollection.class));
    }
}
