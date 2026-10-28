package com.robinloom.jweaver.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KotlinSupportTest {

    @Test
    void testPlainClassIsNotKotlin() {
        class Plain {
        }

        Assertions.assertFalse(KotlinSupport.isKotlinClass(Plain.class));
    }

    @Test
    void testClassWithUnrelatedAnnotationIsNotKotlin() {
        @Deprecated
        class Annotated {
        }

        Assertions.assertFalse(KotlinSupport.isKotlinClass(Annotated.class));
    }

    @Test
    void testClassAnnotatedWithKotlinMetadataIsKotlin() {
        @kotlin.Metadata
        class KotlinLike {
        }

        Assertions.assertTrue(KotlinSupport.isKotlinClass(KotlinLike.class));
    }
}
