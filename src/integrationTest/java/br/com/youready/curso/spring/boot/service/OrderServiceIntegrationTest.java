package br.com.youready.curso.spring.boot.service;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.youready.curso.spring.boot.BaseIntegrationTest;
import br.com.youready.curso.spring.boot.TestModelFactory;
import br.com.youready.curso.spring.boot.model.dto.OrderItemRequest;
import br.com.youready.curso.spring.boot.model.dto.OrderRequest;
import br.com.youready.curso.spring.boot.model.dto.OrderResponse;
import br.com.youready.curso.spring.boot.model.entity.Product;
import br.com.youready.curso.spring.boot.repository.ProductRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("Order Service Integration Tests")
class OrderServiceIntegrationTest extends BaseIntegrationTest {

  @Autowired private OrderService orderService;

  @Autowired private ProductRepository productRepository;

  private Product product;

  @BeforeEach
  void setUp() {
    restartTestDB();
    product = TestModelFactory.createProduct();
    product.setPrice(new BigDecimal("60.00"));
    product.setStockQuantity(100);
    product = productRepository.save(product);
  }

  @Test
  @DisplayName("Standard Shipping: Should NOT apply free shipping for orders of R$ 100.00 or less")
  void shouldNotApplyFreeShippingWhenAmountIsLow() {
    // given
    var orderItemRequest = new OrderItemRequest(product.getId(), 1); // Total: 60.00
    OrderRequest orderRequest = TestModelFactory.createOrderRequest(orderItemRequest);

    // when
    OrderResponse response = orderService.placeOrder(orderRequest);

    // then
    assertThat(response.freeShipping()).isFalse();
    assertThat(response.totalAmount()).isEqualByComparingTo("60.00");
  }

  @Test
  @DisplayName(
      "Standard Shipping: Should NOT apply free shipping when total amount is exactly R$ 100.00")
  void shouldNotApplyFreeShippingWhenAmountIsExactly100() {
    // given
    product.setPrice(new BigDecimal("100.00"));
    productRepository.save(product);

    var orderItemRequest = new OrderItemRequest(product.getId(), 1); // Total: 100.00
    OrderRequest orderRequest = TestModelFactory.createOrderRequest(orderItemRequest);

    // when
    OrderResponse response = orderService.placeOrder(orderRequest);

    // then
    assertThat(response.freeShipping()).isFalse();
    assertThat(response.totalAmount()).isEqualByComparingTo("100.00");
  }

  @Test
  @DisplayName("Standard Shipping: Should apply free shipping for orders above R$ 100.00")
  void shouldApplyFreeShippingWhenAmountIsHigh() {
    // given
    var orderItemRequest = new OrderItemRequest(product.getId(), 2); // Total: 120.00
    OrderRequest orderRequest = TestModelFactory.createOrderRequest(orderItemRequest);

    // when
    OrderResponse response = orderService.placeOrder(orderRequest);

    // then
    assertThat(response.freeShipping()).isTrue();
    assertThat(response.totalAmount()).isEqualByComparingTo("120.00");
  }
}
