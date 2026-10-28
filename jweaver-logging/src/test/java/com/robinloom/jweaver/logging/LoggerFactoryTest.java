package com.robinloom.jweaver.logging;

import com.robinloom.jweaver.Mode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

class LoggerFactoryTest {

    @Test
    void testGetLoggerByClassWrapsSlf4jLoggerWithInlineMode() {
        Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);

        Assertions.assertInstanceOf(JWeaverLogger.class, logger);
        Assertions.assertEquals(LoggerFactoryTest.class.getName(), logger.getName());
    }

    @Test
    void testGetLoggerByClassAndModeWrapsSlf4jLogger() {
        Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class, Mode.TREE);

        Assertions.assertInstanceOf(JWeaverLogger.class, logger);
        Assertions.assertEquals(LoggerFactoryTest.class.getName(), logger.getName());
    }

    @Test
    void testGetLoggerByNameWrapsSlf4jLoggerWithInlineMode() {
        Logger logger = LoggerFactory.getLogger("custom-logger");

        Assertions.assertInstanceOf(JWeaverLogger.class, logger);
        Assertions.assertEquals("custom-logger", logger.getName());
    }

    @Test
    void testGetLoggerByNameAndModeWrapsSlf4jLogger() {
        Logger logger = LoggerFactory.getLogger("custom-logger", Mode.TREE);

        Assertions.assertInstanceOf(JWeaverLogger.class, logger);
        Assertions.assertEquals("custom-logger", logger.getName());
    }
}
