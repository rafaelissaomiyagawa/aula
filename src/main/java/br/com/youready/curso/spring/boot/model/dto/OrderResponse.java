package br.com.youready.curso.spring.boot.model.dto;

import br.com.youready.curso.spring.boot.model.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    Long id,
    String orderNumber,
    String customerEmail,
    LocalDateTime orderDate,
    OrderStatus status,
    BigDecimal totalAmount,
    boolean freeShipping,
    boolean manualReview,
    List<OrderItemResponse> items) {
  public record OrderItemResponse(
      Long productId, String productName, int quantity, BigDecimal unitPrice) {}
}
