package com.example;

import com.example.exception.BusinessRuleException;
import com.example.model.dto.OrderItemRequest;
import com.example.model.dto.OrderRequest;
import com.example.model.entity.Product;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.service.InventoryService;
import com.example.service.KafkaProducerService;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class InventoryServiceUnitTest {

    @Test
    void b() {
        Product product = new Product();
        product.setStockQuantity(10);

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
                  .isThrownBy(() -> product.validateStockQuantity(30))
                  .withMessageContaining("Not enough stock for product");
    }

    @Test
    void a() {

        OrderItemRequest orderItemRequest = Instancio.of(TestModelFactory.ORDER_ITEM_REQUEST_MODEL)
                                                     .set(Select.field(OrderItemRequest::quantity), 50)
                                                     .create();
        OrderRequest orderRequest = new OrderRequest("teste@teste.com", List.of(orderItemRequest));
        Product product = TestModelFactory.createProduct();

        product.setStockQuantity(10);

        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
        KafkaProducerService kafkaProducerService = Mockito.mock(KafkaProducerService.class);

        Mockito.when(productRepository.findById(orderItemRequest.productId()))
               .thenReturn(Optional.of(product));

        InventoryService inventoryService = new InventoryService(productRepository, orderRepository, kafkaProducerService);

        Assertions.assertThatExceptionOfType(BusinessRuleException.class)
                  .isThrownBy(() -> inventoryService.placeOrder(orderRequest))
                  .withMessageContaining("Not enough stock for product");
    }
}
