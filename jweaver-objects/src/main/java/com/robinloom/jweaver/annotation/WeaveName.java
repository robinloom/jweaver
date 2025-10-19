package com.robinloom.jweaver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Overrides the displayed name of a field, method, or record component
 * in JWeaver's textual output.
 * <p>
 * This is useful when the internal field name differs from the desired
 * representation, or when you want to abbreviate or localize property names.
 * <p>
 * When present, {@code @WeaveName} takes precedence over the default
 * field or accessor name determined by reflection.
 *
 * <pre>{@code
 * public class Address {
 *     @WeaveName("zip")
 *     private String postalCode;
 * }
 * }</pre>
 *
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WeaveName {
    String value();
}
