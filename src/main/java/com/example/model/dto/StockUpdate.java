package com.example.model.dto;

import java.io.Serializable;

public record StockUpdate(Long productId, Integer quantity) implements Serializable {
}
