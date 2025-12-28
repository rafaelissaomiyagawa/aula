package br.com.youready.curso.spring.boot.event;

import br.com.youready.curso.spring.boot.model.entity.Order;

public record OrderPlacedEvent(Order order) {}
