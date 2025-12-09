package com.example.simple;

import com.example.EndpointTest;
import com.example.TestModelFactory;
import com.example.model.dto.OrderItemRequest;
import com.example.model.dto.OrderRequest;
import com.example.model.entity.Product;
import com.example.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Sql(scripts = "/sql/insert-product-and-order.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MockMvcExampleTest extends EndpointTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductRepository productRepository;

    @Test
    void placeOrder_happyPath() throws Exception {
        // Arrange
        Product product = productRepository.save(TestModelFactory.createProduct());
        OrderItemRequest orderItemRequest = TestModelFactory.createOrderItemRequest(product);
        OrderRequest orderRequest = TestModelFactory.createOrderRequest(orderItemRequest);


        // Act & Assert
        // Eu garanto que a resposta vai ser assim (parte de contrato)
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"));

        // Ordem
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStockQuantity())
                .isEqualTo(product.getStockQuantity() - orderItemRequest.quantity());
    }
}
