package com.example.model.dto;

import com.example.model.enums.ProductCategory;
import java.math.BigDecimal;

public record ProductResponse(
    Long id,
    String sku,
    String name,
    BigDecimal price,
    ProductCategory category,
    Integer stockQuantity,
    boolean active
) {}

