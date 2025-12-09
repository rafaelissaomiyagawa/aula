package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("A Guide to JUnit 5 Annotations")
class JUnitExampleTest {

    static class Calculator {
        int add(int a, int b) {
            return a + b;
        }

        int subtract(int a, int b) {
            return a - b;
        }
    }

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
        System.out.println("Executing @BeforeEach: A new calculator is ready.");
    }

    @Nested
    @DisplayName("Tests for the Addition Feature")
    class AdditionTests {

        @Test
        @DisplayName("1 + 1 = 2")
        void testSimpleAddition() {
            assertEquals(2, calculator.add(1, 1), "1 + 1 should equal 2");
        }

        @ParameterizedTest(name = "Adding {0} and {1} should result in {2}")
        @CsvSource({
                "0,   1,   1",
                "1,   2,   3",
                "-5,  5,   0",
                "-1, -1,  -2",
                "100, 200, 300"
        })
        @DisplayName("Test multiple additions using @ParameterizedTest")
        void testMultipleAdditions(int a, int b, int expectedResult) {
            assertEquals(expectedResult, calculator.add(a, b),
                    () -> "Adding " + a + " and " + b + " did not produce " + expectedResult);
        }
    }

    @Nested
    @DisplayName("Tests for the Subtraction Feature")
    class SubtractionTests {

        @Test
        @DisplayName("5 - 3 = 2")
        void testSimpleSubtraction() {
            assertEquals(2, calculator.subtract(5, 3));
        }
    }
}
