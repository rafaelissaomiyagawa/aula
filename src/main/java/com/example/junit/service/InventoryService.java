package com.example.junit.service;

import com.example.junit.exception.BusinessRuleException;
import com.example.junit.model.dto.OrderRequest;
import com.example.junit.model.dto.OrderResponse;
import com.example.junit.model.dto.ProductResponse;
import com.example.junit.model.entity.Order;
import com.example.junit.model.entity.OrderItem;
import com.example.junit.model.entity.Product;
import com.example.junit.model.enums.OrderStatus;
import com.example.junit.model.enums.ProductCategory;
import com.example.junit.repository.OrderRepository;
import com.example.junit.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse placeOrder(OrderRequest request) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setCustomerEmail(request.customerEmail());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new BusinessRuleException("Product not found with id: " + itemRequest.productId()));

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
            
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);
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
        return toOrderResponse(savedOrder);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessRuleException("Order not found with id: " + orderId));

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new BusinessRuleException("Cannot cancel an order that has already been shipped or delivered.");
        }

        order.setStatus(OrderStatus.CANCELLED);

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }
        orderRepository.save(order);
        log.info("Order {} has been cancelled.", order.getOrderNumber());
    }

    public BigDecimal processRefund(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessRuleException("Order not found with id: " + orderId));

        OrderItem itemToRefund = order.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("Order item not found with id: " + itemId));

        Product product = itemToRefund.getProduct();
        BigDecimal refundAmount = itemToRefund.getUnitPrice().multiply(BigDecimal.valueOf(itemToRefund.getQuantity()));

        if (product.getCategory() == ProductCategory.GROCERY && order.getOrderDate().isBefore(LocalDateTime.now().minusDays(2))) {
            throw new BusinessRuleException("Grocery items cannot be returned after 2 days.");
        }

        if (product.getCategory() == ProductCategory.ELECTRONICS) {
            BigDecimal restockingFee = refundAmount.multiply(new BigDecimal("0.20"));
            refundAmount = refundAmount.subtract(restockingFee);
            log.info("Applied 20% restocking fee for electronics refund on order {}.", order.getOrderNumber());
        }
        
        // Return item to stock
        product.setStockQuantity(product.getStockQuantity() + itemToRefund.getQuantity());
        productRepository.save(product);

        log.info("Refund for item {} on order {} processed. Amount: {}", itemToRefund.getId(), order.getOrderNumber(), refundAmount);
        return refundAmount;
    }


    public void updateStock(Long productId, Integer quantityChange) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessRuleException("Product not found with id: " + productId));

        int newStock = product.getStockQuantity() + quantityChange;
        if (newStock < 0) {
            throw new BusinessRuleException("Stock quantity cannot be negative.");
        }
        product.setStockQuantity(newStock);
        productRepository.save(product);
        log.info("Stock for product {} updated to {}.", product.getName(), newStock);
    }

    public List<ProductResponse> getLowStockProducts() {
        return productRepository.findLowStockProducts().stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerEmail(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount(),
                order.isFreeShipping(),
                order.isManualReview(),
                order.getItems().stream().map(this::toOrderItemResponse).collect(Collectors.toList())
        );
    }

    private OrderResponse.OrderItemResponse toOrderItemResponse(OrderItem item) {
        return new OrderResponse.OrderItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice()
        );
    }
    
    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getSku(),
            product.getName(),
            product.getPrice(),
            product.getCategory(),
            product.getStockQuantity(),
            product.isActive()
        );
    }
}
