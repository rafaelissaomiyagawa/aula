package br.com.youready.curso.spring.boot.repository;

import br.com.youready.curso.spring.boot.model.entity.Product;
import br.com.youready.curso.spring.boot.model.enums.ProductCategory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

  Optional<Product> findByName(String name);

  // Exercise 05 - Part 1: Category Filtering
  List<Product> findByCategory(ProductCategory category);

  // Exercise 05 - Part 2: Price Range Search
  List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

  // Exercise 05 - Part 3: Name Search (Partial Match & Case Insensitive)
  List<Product> findByNameContainingIgnoreCase(String name);

  // Exercise 05 - Part 4: Top Expensive Products
  List<Product> findTop5ByOrderByPriceDesc();

  // Re-adding missing method (using derived query equivalent)
  @Query("SELECT p FROM Product p WHERE p.stockQuantity < 10")
  List<Product> findLowStockProducts();
}
