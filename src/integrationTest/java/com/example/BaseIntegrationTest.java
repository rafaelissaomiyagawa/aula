package com.example;

import com.example.config.TestConfig;
import com.example.framework.TestDataInitializer;
import com.example.framework.TestDatabaseManager;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import javax.sql.DataSource;

import static com.example.framework.TestDatabaseManager.PGSQL_CONTAINER;

@SpringBootTest
@EmbeddedKafka(
        topics = {"stock-topic"}
)
@Import(TestConfig.class)
public class BaseIntegrationTest {

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected TestDataInitializer testDataInitializer;

    protected void restartTestDB() {
        TestDatabaseManager.restartTestDB();
        resetConnectionPool();
    }

    private void resetConnectionPool() {
        // Reset Hikari pool before each test to ensure fresh connections
        if (dataSource instanceof HikariDataSource hds) {
            HikariPoolMXBean pool = hds.getHikariPoolMXBean();
            if (pool != null) {
                pool.softEvictConnections();
            }
        }
    }


    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", PGSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", PGSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", PGSQL_CONTAINER::getPassword);
    }
}
