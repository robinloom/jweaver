package kotlin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Minimal stand-in for Kotlin's kotlin.Metadata annotation, letting tests simulate a
// Kotlin-compiled class without depending on the actual Kotlin stdlib.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Metadata {
}
