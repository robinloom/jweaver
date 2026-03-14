package com.robinloom.jweaver.logging;

import com.robinloom.jweaver.Mode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.logging.Level;

import static org.mockito.Mockito.verify;

public class JWeaverLoggerTest {

    @Test
    void shouldDelegateAllLoggerMethods() throws Exception {
        Logger delegate = Mockito.mock(Logger.class);
        JWeaverLogger wrapper = new JWeaverLogger(delegate, Mode.INLINE);

        for (Method method : Logger.class.getMethods()) {

            // skip default / static methods
            if (Modifier.isStatic(method.getModifiers())) continue;
            if (method.getDeclaringClass() == Object.class) continue;
            if (method.isDefault()) continue;

            Object[] args = createArguments(method.getParameterTypes());

            // invoke on wrapper
            method.invoke(wrapper, args);

            // verify delegate call
            method.invoke(verify(delegate), args);

            // reset for next iteration
            Mockito.reset(delegate);
        }
    }

    private Object[] createArguments(Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes)
                .map(this::dummy)
                .toArray();
    }

    private Object dummy(Class<?> type) {
        if (type == String.class) return "test-message";
        if (type == Object.class) return "arg";
        if (type == Throwable.class) return new RuntimeException("test");
        if (type == Level.class) return Level.INFO;

        if (type.isArray()) {
            return new Object[]{"a", "b"};
        }

        if (type == boolean.class || type == Boolean.class) return true;
        if (type.isPrimitive()) return 0;

        return null;
    }

}
