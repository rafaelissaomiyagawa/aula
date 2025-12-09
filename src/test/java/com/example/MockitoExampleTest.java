package com.example;

import com.example.exception.BusinessRuleException;
import com.example.model.dto.OrderItemRequest;
import com.example.model.dto.OrderRequest;
import com.example.model.dto.OrderResponse;
import com.example.model.entity.Order;
import com.example.model.entity.OrderItem;
import com.example.model.entity.Product;
import com.example.model.enums.OrderStatus;
import com.example.repository.OrderRepository;
import com.example.repository.ProductRepository;
import com.example.service.InventoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@DisplayName("A Guide to Mockito and BDDMockito")
@ExtendWith(MockitoExtension.class)
class MockitoExampleTest {

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;


    @Nested
    @DisplayName("Standard Mockito Style")
    class StandardMockito {

        @Test
        @DisplayName("should place an order successfully when stock is available")
        void testPlaceOrder_Success() {
            OrderRequest orderRequest = new OrderRequest("test@example.com", List.of(new OrderItemRequest(1L, 5)));
            Product product = new Product();
            product.setId(1L);
            product.setName("Test Product");
            product.setPrice(new BigDecimal("10.00"));
            product.setStockQuantity(10);

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

            OrderResponse response = inventoryService.placeOrder(orderRequest);

            verify(productRepository, times(1)).findById(1L);
            verify(orderRepository, times(1)).save(any(Order.class));

            assertThat(response).isNotNull();
            assertThat(response.status()).isEqualTo(OrderStatus.PENDING);
            assertThat(response.totalAmount()).isEqualByComparingTo("45.00");
        }

        @Test
        @DisplayName("should capture the saved Order object and verify its content")
        void testPlaceOrder_CaptureArgument() {
            OrderRequest orderRequest = new OrderRequest("capture@example.com", List.of(new OrderItemRequest(1L, 3)));
            Product product = new Product();
            product.setId(1L);
            product.setName("Captured Product");
            product.setPrice(new BigDecimal("15.00"));
            product.setStockQuantity(10);

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

            inventoryService.placeOrder(orderRequest);

            ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
            verify(orderRepository).save(orderCaptor.capture());

            Order capturedOrder = orderCaptor.getValue();

            assertThat(capturedOrder).isNotNull();
            assertThat(capturedOrder.getCustomerEmail()).isEqualTo("capture@example.com");
            assertThat(capturedOrder.getStatus()).isEqualTo(OrderStatus.PENDING);
            assertThat(capturedOrder.getTotalAmount()).isEqualByComparingTo("45.00"); // 3 * 15.00 = 45.00
            assertThat(capturedOrder.getItems()).hasSize(1);
            assertThat(capturedOrder.getItems().getFirst().getProduct().getName()).isEqualTo("Captured Product");
            assertThat(capturedOrder.getItems().getFirst().getQuantity()).isEqualTo(3);
        }

        @Test
        @DisplayName("should throw exception when product does not exist")
        void testPlaceOrder_ProductNotFound() {
            when(productRepository.findById(1L)).thenReturn(Optional.empty());
            OrderRequest orderRequest = new OrderRequest("test@example.com", Collections.singletonList(new OrderItemRequest(1L, 1)));

            assertThatThrownBy(() -> inventoryService.placeOrder(orderRequest))
                    .isInstanceOf(BusinessRuleException.class)
                    .hasMessage("Product not found with id: 1");

            verify(orderRepository, never()).save(any(Order.class));
        }
    }


    @Nested
    @DisplayName("BDD (Behavior-Driven Development) Mockito Style")
    class BddMockito {

        @Test
        @DisplayName("should cancel an order and restock items")
        void testCancelOrder_BddStyle() {
            Order order = new Order();
            order.setId(1L);
            order.setStatus(OrderStatus.PENDING);
            Product product = new Product();
            product.setStockQuantity(5);
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(2);
            item.setUnitPrice(BigDecimal.TEN);
            order.addItem(item);


            BDDMockito.given(orderRepository.findById(1L)).willReturn(Optional.of(order));

            inventoryService.cancelOrder(1L);

            BDDMockito.then(orderRepository).should(times(1)).findById(1L);
            BDDMockito.then(orderRepository).should().save(any(Order.class));
            BDDMockito.then(productRepository).should().save(any(Product.class));

            assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
            assertThat(product.getStockQuantity()).isEqualTo(7);
        }
    }
}
