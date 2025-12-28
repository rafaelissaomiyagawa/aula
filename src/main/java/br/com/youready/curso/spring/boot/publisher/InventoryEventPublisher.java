package br.com.youready.curso.spring.boot.publisher;

import br.com.youready.curso.spring.boot.model.dto.StockUpdate;
import br.com.youready.curso.spring.boot.model.entity.Order;

public interface InventoryEventPublisher {
  void publishOrderPlaced(Order order);

  void publishStockUpdated(StockUpdate update);
}
