package br.com.youready.curso.spring.boot.controller;

import br.com.youready.curso.spring.boot.model.dto.ProductResponse;
import br.com.youready.curso.spring.boot.model.enums.ProductCategory;
import br.com.youready.curso.spring.boot.service.ProductQueryService;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

  private final ProductQueryService productQueryService;

  @GetMapping
  public ResponseEntity<Page<ProductResponse>> getAllProducts(
      @PageableDefault(size = 10, sort = "name") Pageable pageable) {
    Page<ProductResponse> products = productQueryService.getAllProducts(pageable);
    return ResponseEntity.ok(products);
  }

  @GetMapping("/category")
  public ResponseEntity<List<ProductResponse>> searchByCategory(
      @RequestParam ProductCategory category) {
    return ResponseEntity.ok(productQueryService.searchByCategory(category));
  }

  @GetMapping("/price")
  public ResponseEntity<List<ProductResponse>> searchByPriceRange(
      @RequestParam BigDecimal min, @RequestParam BigDecimal max) {
    return ResponseEntity.ok(productQueryService.searchByPriceRange(min, max));
  }

  @GetMapping("/name")
  public ResponseEntity<List<ProductResponse>> searchByName(@RequestParam String name) {
    return ResponseEntity.ok(productQueryService.searchByName(name));
  }

  @GetMapping("/top-expensive")
  public ResponseEntity<List<ProductResponse>> getTopExpensive() {
    return ResponseEntity.ok(productQueryService.getTopExpensiveProducts());
  }
}
