package com.example.junit;

import com.example.junit.model.entity.Order;
import com.example.junit.model.entity.OrderItem;
import com.example.junit.model.entity.Product;
import com.example.junit.model.enums.OrderStatus;
import com.example.junit.model.enums.ProductCategory;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

// https://www.baeldung.com/java-test-data-instancio
@DisplayName("A Guide to Instancio for Test Data Generation")
class InstancioExampleTest {

    @Test
    @DisplayName("should generate a complete Product object")
    void testGenerateSimpleProduct() {
        Product product = Instancio.create(Product.class);

        assertThat(product).isNotNull();
        assertThat(product.getId()).isNotNull();
        assertThat(product.getName()).isNotBlank();
        assertThat(product.getPrice()).isGreaterThan(BigDecimal.ZERO);
        assertThat(product.getCategory()).isNotNull();
        assertThat(product.getStockQuantity()).isNotNegative();
        assertThat(product.getSku()).isNotBlank();
        assertThat(product.isActive()).isTrue();
    }

    @Test
    @DisplayName("should generate a Product with specific fields set")
    void testGenerateProductWithSpecificValues() {
        Product product = Instancio.of(Product.class)
                                   .set(Select.field("name"), "Custom Laptop")
                                   .set(Select.field(Product::getSku), "SKU-456")
                                   .set(Select.field(Product::getPrice), new BigDecimal("999.99"))
                                   .set(Select.field(Product::getCategory), ProductCategory.ELECTRONICS)
                                   .set(Select.field(Product::getStockQuantity), 50)
                                   .ignore(Select.field(Product::getId))
                                   .create();

        assertThat(product).isNotNull();
        assertThat(product.getId()).isNull();
        assertThat(product.getName()).isEqualTo("Custom Laptop");
        assertThat(product.getSku()).isEqualTo("SKU-456");
        assertThat(product.getPrice()).isEqualByComparingTo("999.99");
        assertThat(product.getCategory()).isEqualTo(ProductCategory.ELECTRONICS);
        assertThat(product.getStockQuantity()).isEqualTo(50);
    }

    @Test
    @DisplayName("should generate a list of Order objects with a specified size")
    void testGenerateListOfOrders() {
        List<Order> orders = Instancio.ofList(Order.class)
                                      .size(3)
                                      .create();

        assertThat(orders).isNotNull()
                          .hasSize(3);
        assertThat(orders.getFirst()
                         .getOrderNumber()).isNotBlank();
        assertThat(orders.getFirst()
                         .getItems()).isNotEmpty();
    }

    @Test
    @DisplayName("should generate an Order ignoring its associated OrderItems")
    void testGenerateOrderIgnoringItems() {
        Order order = Instancio.of(Order.class)
                               .ignore(Select.field(Order::getItems))
                               .create();

        assertThat(order).isNotNull();
        assertThat(order.getOrderNumber()).isNotBlank();
        assertThat(order.getItems()).isEmpty();
    }

    @Test
    @DisplayName("should generate an Order with custom properties for nested Product in OrderItem")
    void testGenerateOrderWithSpecificNestedProduct() {
        Order order = Instancio.of(Order.class)
                               .onComplete(Select.field(OrderItem::getProduct), (Product product) -> {
                                   product.setName("Specific Ordered Item");
                                   product.setCategory(ProductCategory.BOOKS);
                               })
                               .create();

        assertThat(order).isNotNull();
        assertThat(order.getItems()).isNotEmpty();
        assertThat(order.getItems()
                        .getFirst()
                        .getProduct()).isNotNull();
        assertThat(order.getItems()
                        .getFirst()
                        .getProduct()
                        .getName()).isEqualTo("Specific Ordered Item");
        assertThat(order.getItems()
                        .getFirst()
                        .getProduct()
                        .getCategory()).isEqualTo(ProductCategory.BOOKS);

        assertThat(order.getCustomerEmail()).isNotBlank();
        assertThat(order.getItems()
                        .getFirst()
                        .getProduct()
                        .getPrice()).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("should generate a Product with a specific enum category")
    void testGenerateProductWithSpecificEnum() {
        Product product = Instancio.of(Product.class)
                                   .set(Select.field(Product::getCategory), ProductCategory.GROCERY)
                                   .create();

        assertThat(product.getCategory()).isEqualTo(ProductCategory.GROCERY);
    }

    @Test
    @DisplayName("should generate an Order with a specific status and total amount")
    void testGenerateOrderWithStatusAndTotal() {
        Order order = Instancio.of(Order.class)
                               .set(Select.field(Order::getStatus), OrderStatus.SHIPPED)
                               .set(Select.field(Order::getTotalAmount), new BigDecimal("150.75"))
                               .create();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.SHIPPED);
        assertThat(order.getTotalAmount()).isEqualByComparingTo("150.75");
    }

    @Test
    @DisplayName("should generate a stream of products with controlled values")
    void testGenerateStreamOfProducts() {
        List<Product> products = Instancio.of(Product.class)
                                          .set(Select.field(Product::getPrice), new BigDecimal("10.00"))
                                          .set(Select.field(Product::getCategory), ProductCategory.BOOKS)
                                          .stream()
                                          .limit(5)
                                          .collect(Collectors.toList());

        assertThat(products).hasSize(5);
        assertThat(products)
                .extracting(Product::getPrice)
                .allSatisfy(price ->
                        assertThat(price)
                                .isBetween(new BigDecimal("10.00"), new BigDecimal("100.00"))
                );
    }

    @Nested
    @DisplayName("Using TestModelFactory")
    class UsingTestModelFactory {

        @Test
        @DisplayName("should create a Product using TestModelFactory")
        void shouldCreateProductUsingTestModelFactory() {
            Product product = TestModelFactory.createProduct();

            assertThat(product).isNotNull();
            assertThat(product.getSku()).matches("[A-Z]{3}-\\d{5}");
            assertThat(product.getPrice()).isBetween(new BigDecimal("10"), new BigDecimal("100"));
            assertThat(product.getStockQuantity()).isNotNegative();
            assertThat(product.isActive()).isTrue();
        }

        @Test
        @DisplayName("should create and override Product values using TestModelFactory")
        void shouldOverrideProductValuesUsingTestModelFactory() {
            Product overriddenProduct = Instancio.of(TestModelFactory.PRODUCT_MODEL)
                    .set(Select.field(Product::getName), "Overridden Name")
                    .set(Select.field(Product::getPrice), new BigDecimal("500.00"))
                    .create();

            assertThat(overriddenProduct).isNotNull();
            assertThat(overriddenProduct.getName()).isEqualTo("Overridden Name");
            assertThat(overriddenProduct.getPrice()).isEqualByComparingTo("500.00");
            // Verify other fields are still generated from the base model
            assertThat(overriddenProduct.getSku()).matches("[A-Z]{3}-\\d{5}");
            assertThat(overriddenProduct.getStockQuantity()).isNotNegative();
            assertThat(overriddenProduct.isActive()).isTrue();
        }
    }
}
