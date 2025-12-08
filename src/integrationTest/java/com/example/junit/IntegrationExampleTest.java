package com.example.junit;

import com.example.TestModelFactory;
import com.example.junit.model.dto.OrderItemRequest;
import com.example.junit.model.dto.OrderRequest;
import com.example.junit.model.dto.OrderResponse;
import com.example.junit.model.entity.Product;
import com.example.junit.model.enums.ProductCategory;
import com.example.junit.repository.ProductRepository;
import com.example.junit.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class IntegrationExampleTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void placeOrder_happyPath() {
        // Arrange
        Product product = TestModelFactory.createProduct();
        productRepository.save(product);

        OrderItemRequest orderItemRequest = TestModelFactory.createOrderItemRequest();
        orderItemRequest.setSku(product.getSku());
        orderItemRequest.setQuantity(10);

        OrderRequest orderRequest = TestModelFactory.createOrderRequest();
        orderRequest.setItems(Collections.singletonList(orderItemRequest));

        // Act
        OrderResponse orderResponse = inventoryService.placeOrder(orderRequest);

        // Assert
        assertThat(orderResponse).isNotNull();
        assertThat(orderResponse.getOrderNumber()).isNotNull();
        assertThat(orderResponse.getStatus()).isEqualTo("PENDING");
        assertThat(orderResponse.getTotalAmount()).isEqualByComparingTo(new BigDecimal(orderItemRequest.getQuantity()).multiply(product.getPrice()));

        Product updatedProduct = productRepository.findBySku(product.getSku()).orElseThrow();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(product.getStockQuantity() - 10);
    }
}
