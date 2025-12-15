package com.example.junit.controller;

import com.example.BaseIntegrationTest;
import com.example.TestModelFactory;
import com.example.framework.TestDataInitializer;
import com.example.model.dto.OrderRequest;
import com.example.model.entity.Product;
import com.example.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("Inventory Controller Integration Tests")
public class InventoryControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product legacyCodeBook;
    private Product refactoringBook;

    @BeforeEach
    void setUp() {
        restartTestDB();
        legacyCodeBook = productRepository.findByName(TestDataInitializer.LEGACY_CODE_BOOK_NAME).orElseThrow();
        refactoringBook = productRepository.findByName(TestDataInitializer.REFACTORING_BOOK_NAME).orElseThrow();
    }

    @Test
    @DisplayName("Should place order successfully when given a valid order request")
    void shouldPlaceOrderSuccessfully() throws Exception {
        // given
        var orderItemRequest1 = TestModelFactory.createOrderItemRequest(legacyCodeBook);
        var orderItemRequest2 = TestModelFactory.createOrderItemRequest(refactoringBook);
        OrderRequest orderRequest = TestModelFactory.createOrderRequest(orderItemRequest1, orderItemRequest2);

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderNumber").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerEmail").value(orderRequest.customerEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items.length()").value(2));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when ordering a product with insufficient stock")
    void shouldReturnBadRequestForInsufficientStock() throws Exception {
        // given
        var orderItemRequest = TestModelFactory.createOrderItemRequest(legacyCodeBook);
        // Create an order request with quantity greater than stock
        var orderRequest = new OrderRequest("customer@example.com", List.of(
                new com.example.model.dto.OrderItemRequest(orderItemRequest.productId(), legacyCodeBook.getStockQuantity() + 1)
        ));

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 Bad Request when ordering a product that does not exist")
    void shouldReturnBadRequestForNonExistentProduct() throws Exception {
        // given
        var orderItemRequest = new com.example.model.dto.OrderItemRequest(999L, 1);
        var orderRequest = new OrderRequest("customer@example.com", List.of(orderItemRequest));

        // when
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                // then
                .andExpect(status().isBadRequest());
    }
}
