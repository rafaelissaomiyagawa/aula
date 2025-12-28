package br.com.youready.curso.spring.boot;

import br.com.youready.curso.spring.boot.exception.BusinessRuleException;
import br.com.youready.curso.spring.boot.model.dto.OrderItemRequest;
import br.com.youready.curso.spring.boot.model.dto.OrderRequest;
import br.com.youready.curso.spring.boot.model.entity.Product;
import br.com.youready.curso.spring.boot.publisher.InventoryEventPublisher;
import br.com.youready.curso.spring.boot.repository.OrderRepository;
import br.com.youready.curso.spring.boot.repository.ProductRepository;
import br.com.youready.curso.spring.boot.service.InventoryService;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

    OrderItemRequest orderItemRequest =
        Instancio.of(TestModelFactory.ORDER_ITEM_REQUEST_MODEL)
            .set(Select.field(OrderItemRequest::quantity), 50)
            .create();
    OrderRequest orderRequest = new OrderRequest("teste@teste.com", List.of(orderItemRequest));
    Product product = TestModelFactory.createProduct();

    product.setStockQuantity(10);

    ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    InventoryEventPublisher eventPublisher = Mockito.mock(InventoryEventPublisher.class);

    Mockito.when(productRepository.findById(orderItemRequest.productId()))
        .thenReturn(Optional.of(product));

    InventoryService inventoryService =
        new InventoryService(productRepository, orderRepository, eventPublisher);

    Assertions.assertThatExceptionOfType(BusinessRuleException.class)
        .isThrownBy(() -> inventoryService.placeOrder(orderRequest))
        .withMessageContaining("Not enough stock for product");
  }
}
