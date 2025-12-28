package br.com.youready.curso.spring.boot.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrderRequest(
    @NotEmpty @Email String customerEmail, @NotEmpty @Valid List<OrderItemRequest> items) {}
