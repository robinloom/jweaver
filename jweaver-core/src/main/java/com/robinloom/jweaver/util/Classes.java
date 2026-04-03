package com.robinloom.jweaver.util;

public final class Classes {

    private Classes() {}

    public static ClassCheck is(Class<?> clazz) {
        return new ClassCheck(clazz);
    }

    public static class ClassCheck {

        private final Class<?> clazz;

        public ClassCheck(Class<?> clazz) {
            this.clazz = clazz;
        }

        public boolean subclassOf(Class<?> possibleSuperClass) {
            return possibleSuperClass.isAssignableFrom(clazz);
        }

        public boolean noSubclassOf(Class<?> possibleSuperClass) {
            return !subclassOf(possibleSuperClass);
        }

        public boolean exactly(Class<?> possibleMatchingClass) {
            return possibleMatchingClass == clazz;
        }
    }
}
