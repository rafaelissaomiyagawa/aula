package com.example.junit.config;

import com.example.junit.model.entity.Product;
import com.example.junit.model.enums.ProductCategory;
import com.example.junit.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            log.info("Initializing product data...");

            Product p1 = new Product();
            p1.setSku("ELC-10001");
            p1.setName("Laptop");
            p1.setPrice(new BigDecimal("1200.00"));
            p1.setCategory(ProductCategory.ELECTRONICS);
            p1.setStockQuantity(10);
            p1.setActive(true);

            Product p2 = new Product();
            p2.setSku("CLT-20002");
            p2.setName("T-Shirt");
            p2.setPrice(new BigDecimal("25.00"));
            p2.setCategory(ProductCategory.CLOTHING);
            p2.setStockQuantity(50);
            p2.setActive(true);

            Product p3 = new Product();
            p3.setSku("GRO-30003");
            p3.setName("Organic Apples");
            p3.setPrice(new BigDecimal("3.50"));
            p3.setCategory(ProductCategory.GROCERY);
            p3.setStockQuantity(100);
            p3.setActive(true);

            Product p4 = new Product();
            p4.setSku("BOK-40004");
            p4.setName("Spring Boot in Action");
            p4.setPrice(new BigDecimal("45.00"));
            p4.setCategory(ProductCategory.BOOKS);
            p4.setStockQuantity(20);
            p4.setActive(true);
            
            Product p5 = new Product();
            p5.setSku("ELC-10005");
            p5.setName("Smartphone");
            p5.setPrice(new BigDecimal("800.00"));
            p5.setCategory(ProductCategory.ELECTRONICS);
            p5.setStockQuantity(5);
            p5.setActive(true);

            Product p6 = new Product();
            p6.setSku("CLT-20006");
            p6.setName("Jeans");
            p6.setPrice(new BigDecimal("60.00"));
            p6.setCategory(ProductCategory.CLOTHING);
            p6.setStockQuantity(30);
            p6.setActive(true);

            Product p7 = new Product();
            p7.setSku("GRO-30007");
            p7.setName("Milk (1L)");
            p7.setPrice(new BigDecimal("2.00"));
            p7.setCategory(ProductCategory.GROCERY);
            p7.setStockQuantity(80);
            p7.setActive(true);

            Product p8 = new Product();
            p8.setSku("BOK-40008");
            p8.setName("Clean Code");
            p8.setPrice(new BigDecimal("35.00"));
            p8.setCategory(ProductCategory.BOOKS);
            p8.setStockQuantity(15);
            p8.setActive(true);

            productRepository.saveAll(List.of(p1, p2, p3, p4, p5, p6, p7, p8));
            log.info("Product data initialized successfully.");
        } else {
            log.info("Product data already exists, skipping initialization.");
        }
    }
}
