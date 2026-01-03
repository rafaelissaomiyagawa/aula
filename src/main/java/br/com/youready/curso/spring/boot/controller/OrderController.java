package br.com.youready.curso.spring.boot.controller;

import br.com.youready.curso.spring.boot.model.dto.OrderRequest;
import br.com.youready.curso.spring.boot.model.dto.OrderResponse;
import br.com.youready.curso.spring.boot.service.OrderService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/{orderNumber}")
  public ResponseEntity<OrderResponse> findByOrderNumber(@PathVariable String orderNumber) {
    return ResponseEntity.ok(orderService.findByOrderNumber(orderNumber));
  }

  @PostMapping
  public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest orderRequest) {
    OrderResponse orderResponse = orderService.placeOrder(orderRequest);
    return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
  }

  @PutMapping("/{id}/cancel")
  public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
    orderService.cancelOrder(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{id}/refund")
  public ResponseEntity<Map<String, BigDecimal>> processRefund(
      @PathVariable("id") Long orderId, @RequestBody Map<String, Long> payload) {
    Long itemId = payload.get("itemId");
    BigDecimal refundAmount = orderService.processRefund(orderId, itemId);
    return ResponseEntity.ok(Map.of("refundAmount", refundAmount));
  }
}
