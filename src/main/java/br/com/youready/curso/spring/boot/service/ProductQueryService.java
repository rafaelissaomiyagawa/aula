package br.com.youready.curso.spring.boot.service;

import br.com.youready.curso.spring.boot.model.dto.ProductResponse;
import br.com.youready.curso.spring.boot.model.entity.Product;
import br.com.youready.curso.spring.boot.model.enums.ProductCategory;
import br.com.youready.curso.spring.boot.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {

  private static final Logger log = LoggerFactory.getLogger(ProductQueryService.class);

  private final ProductRepository productRepository;

  public Page<ProductResponse> getAllProducts(Pageable pageable) {
    return productRepository.findAll(pageable).map(Product::toProductResponse);
  }

  public List<ProductResponse> searchByCategory(ProductCategory category) {
    return productRepository.findByCategory(category).stream()
        .map(Product::toProductResponse)
        .collect(Collectors.toList());
  }

  public List<ProductResponse> searchByPriceRange(BigDecimal min, BigDecimal max) {
    return productRepository.findByPriceBetween(min, max).stream()
        .map(Product::toProductResponse)
        .collect(Collectors.toList());
  }

  public List<ProductResponse> searchByName(String name) {
    return productRepository.findByNameContainingIgnoreCase(name).stream()
        .map(Product::toProductResponse)
        .collect(Collectors.toList());
  }

  public List<ProductResponse> getTopExpensiveProducts() {
    return productRepository.findTop5ByOrderByPriceDesc().stream()
        .map(Product::toProductResponse)
        .collect(Collectors.toList());
  }

  public List<ProductResponse> getLowStockProducts() {
    return productRepository.findLowStockProducts().stream()
        .map(Product::toProductResponse)
        .collect(Collectors.toList());
  }
}
