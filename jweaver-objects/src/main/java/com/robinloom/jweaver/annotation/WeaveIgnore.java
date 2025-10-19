package com.robinloom.jweaver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excludes a field, method, or record component from being rendered by JWeaver.
 * <p>
 * Use {@code @WeaveIgnore} to suppress properties that are not relevant for
 * textual output or that would otherwise clutter the rendered representation.
 * This annotation is evaluated by all {@link com.robinloom.jweaver.commons.Weaver}
 * implementations and takes precedence over other weave-related annotations.
 * <p>
 * Typical examples include internal caches, derived values, or references that
 * would cause redundant output.
 *
 * <pre>{@code
 * public class User {
 *     private String name;
 *
 *     @WeaveIgnore
 *     private Session currentSession;
 * }
 * }</pre>
 *
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WeaveIgnore {
}
