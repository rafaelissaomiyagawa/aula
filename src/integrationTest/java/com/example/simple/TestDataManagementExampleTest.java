package com.example.simple;

import com.example.model.entity.Order;
import com.example.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class TestDataManagementExampleTest {

    @Autowired
    private OrderRepository orderRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.5");

    @Test
    @Sql(scripts = "/sql/insert-product-and-order.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getOrder_withSqlData() {
        // Arrange: Data is inserted by the 'insert-product-and-order.sql' script

        // Act
        Order order = orderRepository.findById(999L).orElseThrow();

        // Assert
        assertThat(order).isNotNull();
        assertThat(order.getOrderNumber()).isEqualTo("SQL-ORDER-1");
        assertThat(order.getCustomerEmail()).isEqualTo("sql-user@example.com");
    }

    @Test
    @Sql(scripts = "/sql/insert-product-and-order.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/cleanup-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getOrder_withSqlData2() {
        // Arrange: Data is inserted by the 'insert-product-and-order.sql' script

        // Act
        Order order = orderRepository.findById(999L).orElseThrow();

        // Assert
        assertThat(order).isNotNull();
        assertThat(order.getOrderNumber()).isEqualTo("SQL-ORDER-1");
        assertThat(order.getCustomerEmail()).isEqualTo("sql-user@example.com");
    }
}
