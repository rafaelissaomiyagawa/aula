package br.com.youready.curso.spring.boot.controller;

import br.com.youready.curso.spring.boot.model.dto.OrderRequest;
import br.com.youready.curso.spring.boot.model.dto.OrderResponse;
import br.com.youready.curso.spring.boot.model.dto.ProductResponse;
import br.com.youready.curso.spring.boot.service.InventoryService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class InventoryController {

  private final InventoryService inventoryService;

  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @PostMapping("/orders")
  public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest orderRequest) {
    OrderResponse orderResponse = inventoryService.placeOrder(orderRequest);
    return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
  }

  @PutMapping("/orders/{id}/cancel")
  public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
    inventoryService.cancelOrder(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/orders/{id}/refund")
  public ResponseEntity<Map<String, BigDecimal>> processRefund(
      @PathVariable("id") Long orderId, @RequestBody Map<String, Long> payload) {
    Long itemId = payload.get("itemId");
    BigDecimal refundAmount = inventoryService.processRefund(orderId, itemId);
    return ResponseEntity.ok(Map.of("refundAmount", refundAmount));
  }

  @GetMapping("/products/low-stock")
  public ResponseEntity<List<ProductResponse>> getLowStockProducts() {
    List<ProductResponse> lowStockProducts = inventoryService.getLowStockProducts();
    return ResponseEntity.ok(lowStockProducts);
  }

  @PutMapping("/products/{id}/stock")
  public ResponseEntity<Void> updateStock(
      @PathVariable Long id, @RequestBody Map<String, Integer> payload) {
    Integer quantityChange = payload.get("quantityChange");
    inventoryService.updateStock(id, quantityChange);
    return ResponseEntity.ok().build();
  }
}
