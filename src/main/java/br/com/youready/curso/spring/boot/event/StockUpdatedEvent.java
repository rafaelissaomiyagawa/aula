package br.com.youready.curso.spring.boot.event;

import br.com.youready.curso.spring.boot.model.dto.StockUpdate;

public record StockUpdatedEvent(StockUpdate stockUpdate) {}
