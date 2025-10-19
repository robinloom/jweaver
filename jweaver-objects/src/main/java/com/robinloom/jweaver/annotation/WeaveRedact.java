package com.robinloom.jweaver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Masks the value of a field or record component when rendered by JWeaver.
 * <p>
 * Use {@code @WeaveRedact} to hide sensitive information such as passwords,
 * API keys, tokens, or personally identifiable data. Redacted values will
 * be replaced by a sequence of mask characters (for example {@code ***}).
 * <p>
 *
 * <pre>{@code
 * public class Credentials {
 *     private String username;
 *
 *     @WeaveRedact(maskString = '•')
 *     private String password;
 * }
 * }</pre>
 *
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WeaveRedact {
    String maskString() default "***";
}
