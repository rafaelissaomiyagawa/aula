package br.com.youready.curso.spring.boot.config;

import br.com.youready.curso.spring.boot.model.entity.Product;
import br.com.youready.curso.spring.boot.model.enums.ProductCategory;
import br.com.youready.curso.spring.boot.repository.ProductRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev-init")
public class DataInitializer {

  @Bean
  CommandLineRunner initDatabase(ProductRepository productRepository) {
    return args -> {
      productRepository.save(
          new Product(
              null,
              "PRD-00001",
              "Laptop",
              new BigDecimal("1200.00"),
              ProductCategory.ELECTRONICS,
              10,
              true));
      productRepository.save(
          new Product(
              null,
              "PRD-00002",
              "Coffee Beans",
              new BigDecimal("25.50"),
              ProductCategory.GROCERY,
              10,
              true));
      productRepository.save(
          new Product(
              null,
              "PRD-00003",
              "The Lord of the Rings",
              new BigDecimal("45.99"),
              ProductCategory.BOOKS,
              10,
              true));
    };
  }
}
