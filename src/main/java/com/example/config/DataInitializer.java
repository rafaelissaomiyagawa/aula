package com.example.config;

import com.example.model.entity.Product;
import com.example.model.enums.ProductCategory;
import com.example.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

@Configuration
@Profile("dev-init")
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return args -> {
            productRepository.save(new Product(null, "PRD-00001", "Laptop", new BigDecimal("1200.00"), ProductCategory.ELECTRONICS, 10, true));
            productRepository.save(new Product(null, "PRD-00002", "Coffee Beans", new BigDecimal("25.50"), ProductCategory.GROCERY, 10, true));
            productRepository.save(new Product(null, "PRD-00003", "The Lord of the Rings", new BigDecimal("45.99"), ProductCategory.BOOKS, 10, true));
        };
    }
}
