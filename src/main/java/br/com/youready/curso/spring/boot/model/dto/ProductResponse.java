package br.com.youready.curso.spring.boot.model.dto;

import br.com.youready.curso.spring.boot.model.enums.ProductCategory;
import java.math.BigDecimal;

public record ProductResponse(
    Long id,
    String sku,
    String name,
    BigDecimal price,
    ProductCategory category,
    Integer stockQuantity,
    boolean active) {}
