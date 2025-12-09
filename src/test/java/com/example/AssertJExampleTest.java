package com.example;

import com.example.model.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("A Guide to Common AssertJ Assertions")
class AssertJExampleTest {

    @Nested
    @DisplayName("Object & Boolean Assertions")
    class ObjectAndBooleanAssertions {

        @Test
        @DisplayName("should assert on object properties and booleans")
        void objectAndBooleanAssertionExamples() {
            Product product = new Product();
            product.setName("AssertJ Guide");
            product.setActive(true);

            Product nullProduct = null;

            assertThat(product).isNotNull();
            assertThat(nullProduct).isNull();

            assertThat(product).isInstanceOf(Product.class);
            assertThat(product.isActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("String Assertions")
    class StringAssertions {

        @Test
        @DisplayName("should assert on various string conditions")
        void stringAssertionExamples() {
            String text = "AssertJ is awesome";
            String emptyText = "";

            assertThat(text)
                    .as("Checking properties of the string 'text'") // .as() provides a custom error message
                    .isEqualTo("AssertJ is awesome")
                    .isNotEqualTo("JUnit is awesome")
                    .isEqualToIgnoringCase("assertj is awesome")
                    .startsWith("AssertJ")
                    .endsWith("awesome")
                    .contains("is")
                    .hasSize(18);

            assertThat(emptyText).isEmpty();
            assertThat("  ").isBlank();
        }
    }

    @Nested
    @DisplayName("Number Assertions")
    class NumberAssertions {

        @Test
        @DisplayName("should assert on various number conditions")
        void numberAssertionExamples() {
            int number = 42;
            double floatingPoint = 99.9;

            assertThat(number)
                    .isEqualTo(42)
                    .isNotZero()
                    .isPositive()
                    .isEven()
                    .isGreaterThan(40)
                    .isLessThan(50)
                    .isBetween(40, 50);

            assertThat(floatingPoint)
                    .isCloseTo(100.0, offset(0.1)) // Asserts that 99.9 is close to 100.0 with a 0.1 tolerance
                    .isNotCloseTo(99.0, offset(0.1));
        }
    }

    @Nested
    @DisplayName("Collection Assertions")
    class CollectionAssertions {

        @Test
        @DisplayName("should assert on list properties")
        void listAssertionExamples() {
            List<String> list = Arrays.asList("apple", "banana", "cherry");

            assertThat(list)
                    .isNotNull()
                    .isNotEmpty()
                    .hasSize(3)
                    .contains("banana")
                    .doesNotContain("grape")
                    .containsExactly("apple", "banana", "cherry") // Must be in this exact order
                    .containsExactlyInAnyOrder("cherry", "apple", "banana") // Order does not matter
                    .startsWith("apple")
                    .endsWith("cherry");
        }
    }

    @Nested
    @DisplayName("Map Assertions")
    class MapAssertions {

        @Test
        @DisplayName("should assert on map properties")
        void mapAssertionExamples() {
            Map<String, Integer> map = new HashMap<>();
            map.put("one", 1);
            map.put("two", 2);

            assertThat(map)
                    .isNotEmpty()
                    .hasSize(2)
                    .containsKey("one")
                    .doesNotContainKey("three")
                    .containsValue(2)
                    .doesNotContainValue(3)
                    .containsEntry("one", 1);
        }
    }

    @Nested
    @DisplayName("Exception Assertions")
    class ExceptionAssertions {

        @Test
        @DisplayName("should assert that an exception is thrown")
        void exceptionAssertionExample() {
            Product product = new Product();
            product.setPrice(new BigDecimal("-1.00"));

            assertThatThrownBy(() -> {
                // This is the code that we expect to throw an exception
                if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("Price cannot be negative");
                }
            })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Price cannot be negative")
            .hasMessageContaining("negative");
        }
    }
}
