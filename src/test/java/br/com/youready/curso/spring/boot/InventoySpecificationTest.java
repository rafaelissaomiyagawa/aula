package br.com.youready.curso.spring.boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Inventory Specification")
public class InventoySpecificationTest {

  @Nested
  @DisplayName("Order Placement")
  class OrderPlacement {
    @Test
    @DisplayName("A product must exist to be added to an order")
    void a() {}

    @Test
    @DisplayName("There must be enough stock for a product to be added to an order")
    void b() {}

    //        - A product must exist to be added to an order.
    // - There must be enough stock for a product to be added to an order.
    // - A maximum of 3 units of an electronic product can be ordered at a time.
    // - A 10% discount is applied to non-electronic products when 5 or more units are ordered.
    // - Orders over $100 qualify for free shipping.
    // - Orders over $500 require manual review.
  }

  class OrderCancelation {}
}
