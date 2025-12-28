package br.com.youready.curso.spring.boot.repository;

import br.com.youready.curso.spring.boot.model.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  Optional<Product> findByName(String name);

  @Query("SELECT p FROM Product p WHERE p.stockQuantity < 5")
  List<Product> findLowStockProducts();
}
