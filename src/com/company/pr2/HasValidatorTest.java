package com.company.pr2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HasValidatorTest {
    static class SomeClass implements HasValidator {
        public HashMap<String, HashMap<String, Object>> rules = new HashMap<>() {{
            put("a", new HashMap<>() {{
                put("min", 0);
                put("max", 10);
            }});
            put("c", new HashMap<>() {{
                put("minLength", 5);
            }});
        }};

        int a;
        double b;
        String c;
    }

    @Test
    void setFieldValue() {
        SomeClass obj = new SomeClass();

        obj.setFieldValue("a", 5);
        assertEquals(5, obj.getFieldValue("a"));

        obj.setFieldValue("c", "abc123");
        assertEquals("abc123", obj.getFieldValue("c"));

        assertThrows(IllegalArgumentException.class, () -> obj.setFieldValue("a", 15));

        assertThrows(IllegalArgumentException.class, () -> obj.setFieldValue("c", "abc"));
    }

    @ParameterizedTest
    @MethodSource("validateParams")
    void validate(String name, Object value, boolean result) {
        SomeClass obj = new SomeClass();

        assertEquals(result, obj.validate(name, value));
    }

    private static Stream<Arguments> validateParams() {
        return Stream.of(
                Arguments.of("a", 4, true),
                Arguments.of("a", 10, false),
                Arguments.of("b", 4, true),
                Arguments.of("c", "abc123", true),
                Arguments.of("c", "abc", false)
        );
    }

    @Test
    void validate() {
    }

    @Test
    void getValidationMethod() throws NoSuchMethodException {
        SomeClass obj = new SomeClass();

        assertEquals(obj.getValidationMethod("min").getName(), "min");

        assertThrows(NoSuchMethodException.class, () -> obj.getValidationMethod("abc"));
    }
}