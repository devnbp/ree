package com.company.pr2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {

    public enum SomeEnum { ONE, TWO, THREE }
    public enum AnotherEnum { THREE, FOUR }

    @ParameterizedTest
    @MethodSource("minParams")
    void min(double actual, double min, boolean include, boolean result) {
        assertEquals(result, Validation.min(actual, min, include));
    }

    private static Stream<Arguments> minParams() {
        return Stream.of(
                Arguments.of(0.5, 0, false, true),
                Arguments.of(0, 0, false, false),
                Arguments.of(0.5, 0, true, true),
                Arguments.of(0, 0, true, true),
                Arguments.of(-0.5, 0, false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("maxParams")
    void max(double actual, double min, boolean include, boolean result) {
        assertEquals(result, Validation.max(actual, min, include));
    }

    private static Stream<Arguments> maxParams() {
        return Stream.of(
                Arguments.of(0.5, 5, false, true),
                Arguments.of(5, 5, false, false),
                Arguments.of(0.5, 5, true, true),
                Arguments.of(5, 5, true, true),
                Arguments.of(0.5, 0, false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("minLengthParams")
    void minLength(String str, int minLength, boolean include, boolean result) {
        assertEquals(result, Validation.minLength(str, minLength, include));
    }

    private static Stream<Arguments> minLengthParams() {
        return Stream.of(
                Arguments.of("abc", 2, false, true),
                Arguments.of("abc", 3, false, false),
                Arguments.of("abc", 2, true, true),
                Arguments.of("abc", 3, true, true),
                Arguments.of("abc", 4, false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("maxLengthParams")
    void maxLength(String str, int minLength, boolean include, boolean result) {
        assertEquals(result, Validation.maxLength(str, minLength, include));
    }

    private static Stream<Arguments> maxLengthParams() {
        return Stream.of(
                Arguments.of("abc", 4, false, true),
                Arguments.of("abc", 3, false, false),
                Arguments.of("abc", 4, true, true),
                Arguments.of("abc", 3, true, true),
                Arguments.of("abc", 2, false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("inEnumParams")
    void inEnum(Enum actual, Enum[] values, boolean result) {
        assertEquals(result, Validation.inEnum(actual, values, true));
    }

    private static Stream<Arguments> inEnumParams() {
        return Stream.of(
                Arguments.of(SomeEnum.ONE, SomeEnum.values(), true),
                Arguments.of(SomeEnum.ONE, AnotherEnum.values(), false),
                Arguments.of(SomeEnum.THREE, AnotherEnum.values(), false)
        );
    }
}