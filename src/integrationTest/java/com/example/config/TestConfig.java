package com.example.config;

import com.example.framework.TestDataInitializer;
import com.example.repository.ProductRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public TestDataInitializer testDataInitializer(ProductRepository productRepository) {
        return new TestDataInitializer(productRepository);
    }
}
