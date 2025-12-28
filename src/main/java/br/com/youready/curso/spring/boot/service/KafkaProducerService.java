package br.com.youready.curso.spring.boot.service;

import br.com.youready.curso.spring.boot.model.dto.StockUpdate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final String STOCK_TOPIC = "stock-topic";

  public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendStockUpdate(StockUpdate stockUpdate) {
    kafkaTemplate.send(STOCK_TOPIC, stockUpdate.productId().toString(), stockUpdate);
  }
}
