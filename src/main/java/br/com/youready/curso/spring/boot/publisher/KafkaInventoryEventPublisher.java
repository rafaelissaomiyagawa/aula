package br.com.youready.curso.spring.boot.publisher;

import br.com.youready.curso.spring.boot.model.dto.StockUpdate;
import br.com.youready.curso.spring.boot.model.entity.Order;
import br.com.youready.curso.spring.boot.service.KafkaEventProducer;
import org.springframework.stereotype.Component;

@Component
public class KafkaInventoryEventPublisher implements InventoryEventPublisher {

  private final KafkaEventProducer kafkaEventProducer;

  public KafkaInventoryEventPublisher(KafkaEventProducer kafkaEventProducer) {
    this.kafkaEventProducer = kafkaEventProducer;
  }

  @Override
  public void publishOrderPlaced(Order order) {
    kafkaEventProducer.sendOrder(order);
  }

  @Override
  public void publishStockUpdated(StockUpdate update) {
    kafkaEventProducer.sendStockUpdate(update);
  }
}
