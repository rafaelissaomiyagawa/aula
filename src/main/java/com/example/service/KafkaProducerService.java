package com.example.service;

import com.example.model.dto.StockUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String stockTopic;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate,
                                @Value("${app.kafka.topic.stock-topic}") String stockTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.stockTopic = stockTopic;
    }

    public void sendStockUpdate(StockUpdate stockUpdate) {
        kafkaTemplate.send(stockTopic, stockUpdate.productId().toString(), stockUpdate);
    }
}
