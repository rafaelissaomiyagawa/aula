package br.com.youready.curso.spring.boot.publisher;

import br.com.youready.curso.spring.boot.event.OrderPlacedEvent;
import br.com.youready.curso.spring.boot.event.StockUpdatedEvent;
import br.com.youready.curso.spring.boot.model.dto.StockUpdate;
import br.com.youready.curso.spring.boot.model.entity.Order;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class SpringInventoryEventPublisher implements InventoryEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  public SpringInventoryEventPublisher(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  @Override
  public void publishOrderPlaced(Order order) {
    eventPublisher.publishEvent(new OrderPlacedEvent(order));
  }

  @Override
  public void publishStockUpdated(StockUpdate update) {
    eventPublisher.publishEvent(new StockUpdatedEvent(update));
  }
}
