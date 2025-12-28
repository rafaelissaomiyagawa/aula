package br.com.youready.curso.spring.boot.service;

import br.com.youready.curso.spring.boot.model.dto.StockUpdate;
import br.com.youready.curso.spring.boot.model.entity.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final String STOCK_TOPIC = "stock-topic";
  private static final String ORDER_TOPIC = "order-topic";

  public KafkaEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendStockUpdate(StockUpdate stockUpdate) {
    kafkaTemplate.send(STOCK_TOPIC, stockUpdate.productId().toString(), stockUpdate);
  }

  public void sendOrder(Order order) {
    kafkaTemplate.send(ORDER_TOPIC, order.getOrderNumber(), order);
  }
}