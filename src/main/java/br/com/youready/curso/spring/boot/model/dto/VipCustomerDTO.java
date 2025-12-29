package br.com.youready.curso.spring.boot.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VipCustomerDTO(
    String customerName, Long totalOrders, BigDecimal totalSpent, LocalDateTime lastOrderDate) {}
