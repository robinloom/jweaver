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

            // Object-arg overloads only delegate (and transform their arguments) when
            // the corresponding level is enabled, so all levels must be stubbed as enabled.
            stubAllLevelsEnabled(delegate);

            Object[] args = createArguments(method.getParameterTypes());

            // invoke on wrapper
            method.invoke(wrapper, args);

            // verify delegate call
            method.invoke(verify(delegate), args);

            // reset for next iteration
            Mockito.reset(delegate);
        }
    }

    @Test
    void shouldSkipTransformationAndDelegationWhenLevelDisabled() {
        Logger delegate = Mockito.mock(Logger.class);
        Mockito.when(delegate.isDebugEnabled()).thenReturn(false);
        JWeaverLogger wrapper = new JWeaverLogger(delegate, Mode.INLINE);

        wrapper.debug("some {}", new Object());

        Mockito.verify(delegate, Mockito.never()).debug(Mockito.anyString(), Mockito.any(Object.class));
    }

    @Test
    void shouldSkipDelegationForEveryObjectArgOverloadWhenItsLevelIsDisabled() throws Exception {
        // A fresh, unstubbed mock reports every isXEnabled()/isXEnabled(Marker) as false.
        Logger delegate = Mockito.mock(Logger.class);
        JWeaverLogger wrapper = new JWeaverLogger(delegate, Mode.INLINE);

        for (Method method : Logger.class.getMethods()) {
            if (Modifier.isStatic(method.getModifiers())) continue;
            if (method.getDeclaringClass() == Object.class) continue;
            if (method.isDefault()) continue;
            if (!takesObjectArgument(method)) continue;

            Object[] args = createArguments(method.getParameterTypes());

            method.invoke(wrapper, args);

            method.invoke(Mockito.verify(delegate, Mockito.never()), args);
        }
    }

    private boolean takesObjectArgument(Method method) {
        for (Class<?> type : method.getParameterTypes()) {
            if (type == Object.class || type == Object[].class) return true;
        }
        return false;
    }

    private void stubAllLevelsEnabled(Logger delegate) {
        Mockito.when(delegate.isTraceEnabled()).thenReturn(true);
        Mockito.when(delegate.isTraceEnabled(Mockito.any())).thenReturn(true);
        Mockito.when(delegate.isDebugEnabled()).thenReturn(true);
        Mockito.when(delegate.isDebugEnabled(Mockito.any())).thenReturn(true);
        Mockito.when(delegate.isInfoEnabled()).thenReturn(true);
        Mockito.when(delegate.isInfoEnabled(Mockito.any())).thenReturn(true);
        Mockito.when(delegate.isWarnEnabled()).thenReturn(true);
        Mockito.when(delegate.isWarnEnabled(Mockito.any())).thenReturn(true);
        Mockito.when(delegate.isErrorEnabled()).thenReturn(true);
        Mockito.when(delegate.isErrorEnabled(Mockito.any())).thenReturn(true);
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
