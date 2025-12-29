package br.com.youready.curso.spring.boot.service;

import br.com.youready.curso.spring.boot.exception.BusinessRuleException;
import br.com.youready.curso.spring.boot.model.dto.OrderRequest;
import br.com.youready.curso.spring.boot.model.dto.OrderResponse;
import br.com.youready.curso.spring.boot.model.dto.ProductResponse;
import br.com.youready.curso.spring.boot.model.dto.StockUpdate;
import br.com.youready.curso.spring.boot.model.entity.Order;
import br.com.youready.curso.spring.boot.model.entity.OrderItem;
import br.com.youready.curso.spring.boot.model.entity.Product;
import br.com.youready.curso.spring.boot.model.enums.OrderStatus;
import br.com.youready.curso.spring.boot.model.enums.ProductCategory;
import br.com.youready.curso.spring.boot.publisher.InventoryEventPublisher;
import br.com.youready.curso.spring.boot.repository.OrderRepository;
import br.com.youready.curso.spring.boot.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class InventoryService {

  private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;
  private final InventoryEventPublisher eventPublisher;

  public OrderResponse placeOrder(OrderRequest request) {
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());
    order.setCustomerEmail(request.customerEmail());
    order.setOrderDate(LocalDateTime.now());
    order.setStatus(OrderStatus.PENDING);

    BigDecimal totalAmount = BigDecimal.ZERO;

    for (var itemRequest : request.items()) {
      Product product =
          productRepository
              .findById(itemRequest.productId())
              .orElseThrow(
                  () ->
                      new BusinessRuleException(
                          "Product not found with id: " + itemRequest.productId()));

      product.validateStockQuantity(itemRequest.quantity());

      if (product.getCategory() == ProductCategory.ELECTRONICS && itemRequest.quantity() > 3) {
        throw new BusinessRuleException("Cannot order more than 3 electronics of the same type.");
      }

      int quantity = itemRequest.quantity();
      BigDecimal unitPrice = product.getPrice();

      // Bulk discount
      if (product.getCategory() != ProductCategory.ELECTRONICS && quantity >= 5) {
        unitPrice = unitPrice.multiply(new BigDecimal("0.90")); // 10% discount
      }

      OrderItem orderItem = new OrderItem();
      orderItem.setProduct(product);
      orderItem.setQuantity(quantity);
      orderItem.setUnitPrice(unitPrice);
      order.addItem(orderItem);

      totalAmount = totalAmount.add(unitPrice.multiply(BigDecimal.valueOf(quantity)));

      int newStock = product.getStockQuantity() - quantity;
      product.setStockQuantity(newStock);
      productRepository.save(product);
      eventPublisher.publishStockUpdated(new StockUpdate(product.getId(), newStock));
    }

    // Free shipping
    if (totalAmount.compareTo(new BigDecimal("100")) > 0) {
      order.setFreeShipping(true);
      log.info("Order {} qualifies for free shipping.", order.getOrderNumber());
    }

    // Manual review
    if (totalAmount.compareTo(new BigDecimal("500")) > 0) {
      order.setManualReview(true);
      log.warn("Order {} requires manual review.", order.getOrderNumber());
    }

    order.setTotalAmount(totalAmount);
    Order savedOrder = orderRepository.save(order);
    log.info("Order {} placed successfully.", savedOrder.getOrderNumber());
    eventPublisher.publishOrderPlaced(savedOrder);

    return savedOrder.toOrderResponse();
  }

  public void cancelOrder(Long orderId) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new BusinessRuleException("Order not found with id: " + orderId));

    if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
      throw new BusinessRuleException(
          "Cannot cancel an order that has already been shipped or delivered.");
    }

    order.setStatus(OrderStatus.CANCELLED);

    for (OrderItem item : order.getItems()) {
      Product product = item.getProduct();
      int newStock = product.getStockQuantity() + item.getQuantity();
      product.setStockQuantity(newStock);
      productRepository.save(product);
      eventPublisher.publishStockUpdated(new StockUpdate(product.getId(), newStock));
    }
    orderRepository.save(order);
    log.info("Order {} has been cancelled.", order.getOrderNumber());
  }

  public BigDecimal processRefund(Long orderId, Long itemId) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new BusinessRuleException("Order not found with id: " + orderId));

    OrderItem itemToRefund =
        order.getItems().stream()
            .filter(item -> item.getId().equals(itemId))
            .findFirst()
            .orElseThrow(
                () -> new BusinessRuleException("Order item not found with id: " + itemId));

    Product product = itemToRefund.getProduct();
    BigDecimal refundAmount =
        itemToRefund.getUnitPrice().multiply(BigDecimal.valueOf(itemToRefund.getQuantity()));

    if (product.getCategory() == ProductCategory.GROCERY
        && order.getOrderDate().isBefore(LocalDateTime.now().minusDays(2))) {
      throw new BusinessRuleException("Grocery items cannot be returned after 2 days.");
    }

    if (product.getCategory() == ProductCategory.ELECTRONICS) {
      BigDecimal restockingFee = refundAmount.multiply(new BigDecimal("0.20"));
      refundAmount = refundAmount.subtract(restockingFee);
      log.info(
          "Applied 20% restocking fee for electronics refund on order {}.", order.getOrderNumber());
    }

    // Return item to stock
    int newStock = product.getStockQuantity() + itemToRefund.getQuantity();
    product.setStockQuantity(newStock);
    productRepository.save(product);
    eventPublisher.publishStockUpdated(new StockUpdate(product.getId(), newStock));

    log.info(
        "Refund for item {} on order {} processed. Amount: {}",
        itemToRefund.getId(),
        order.getOrderNumber(),
        refundAmount);
    return refundAmount;
  }

  public void updateStock(Long productId, Integer quantityChange) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(
                () -> new BusinessRuleException("Product not found with id: " + productId));

    int newStock = product.getStockQuantity() + quantityChange;
    if (newStock < 0) {
      throw new BusinessRuleException("Stock quantity cannot be negative.");
    }
    product.setStockQuantity(newStock);
    productRepository.save(product);
    eventPublisher.publishStockUpdated(new StockUpdate(product.getId(), newStock));
    log.info("Stock for product {} updated to {}.", product.getName(), newStock);
  }

  public List<ProductResponse> getLowStockProducts() {
    return productRepository.findLowStockProducts().stream()
        .map(Product::toProductResponse)
        .collect(Collectors.toList());
  }
}
