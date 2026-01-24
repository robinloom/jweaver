package com.robinloom.jweaver.logging;

import com.robinloom.jweaver.Mode;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

public class JWeaverLoggerTest {

    Logger LOGGER_BY_CLASS = LoggerFactory.getLogger(JWeaverLoggerTest.class);
    Logger LOGGER_BY_CLASS_TREE = LoggerFactory.getLogger(JWeaverLoggerTest.class, Mode.TREE);
    Logger LOGGER_BY_NAME = LoggerFactory.getLogger("Test");
    Logger LOGGER_BY_NAME_BULLET = LoggerFactory.getLogger("Test", Mode.BULLET);

    record Person(String name, int age) {}
    record Car(String name, int year) {}
    record Cat(String name, int age) {}

    @Test
    public void testLoggerByClass() {
        Person person = new Person("John", 18);
        Car car = new Car("Volvo", 2012);
        Cat cat = new Cat("Bob", 5);

        LOGGER_BY_CLASS.trace("{}", person);
        LOGGER_BY_CLASS.trace("{}, {}", person, car);
        LOGGER_BY_CLASS.trace("{}, {}, {}", person, car, cat);

        LOGGER_BY_CLASS.debug("{}", person);
        LOGGER_BY_CLASS.debug("{}, {}", person, car);
        LOGGER_BY_CLASS.debug("{}, {}, {}", person, car, cat);

        LOGGER_BY_CLASS.info("{}", person);
        LOGGER_BY_CLASS.info("{}, {}", person, car);
        LOGGER_BY_CLASS.info("{}, {}, {}", person, car, cat);

        LOGGER_BY_CLASS.warn("{}", person);
        LOGGER_BY_CLASS.warn("{}, {}", person, car);
        LOGGER_BY_CLASS.warn("{}, {}, {}", person, car, cat);

        LOGGER_BY_CLASS.error("{}", person);
        LOGGER_BY_CLASS.error("{}, {}", person, car);
        LOGGER_BY_CLASS.error("{}, {}, {}", person, car, cat);
    }

    @Test
    public void testCustomLoggerByClass() {
        Person person = new Person("John", 18);
        LOGGER_BY_CLASS_TREE.trace("\n{}", person);
        LOGGER_BY_CLASS_TREE.debug("\n{}", person);
        LOGGER_BY_CLASS_TREE.info("\n{}", person);
        LOGGER_BY_CLASS_TREE.warn("\n{}", person);
        LOGGER_BY_CLASS_TREE.error("\n{}", person);
    }

    @Test
    public void testLoggerByName() {
        Person person = new Person("John", 18);
        LOGGER_BY_NAME.trace("{}", person);
        LOGGER_BY_NAME.debug("{}", person);
        LOGGER_BY_NAME.info("{}", person);
        LOGGER_BY_NAME.warn("{}", person);
        LOGGER_BY_NAME.error("{}", person);
    }

    @Test
    public void testCustomLoggerByName() {
        Person person = new Person("John", 18);
        LOGGER_BY_NAME_BULLET.trace("\n{}", person);
        LOGGER_BY_NAME_BULLET.debug("\n{}", person);
        LOGGER_BY_NAME_BULLET.info("\n{}", person);
        LOGGER_BY_NAME_BULLET.warn("\n{}", person);
        LOGGER_BY_NAME_BULLET.error("\n{}", person);
    }

}
