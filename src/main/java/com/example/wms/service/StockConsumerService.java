package com.example.wms.service;

import com.example.model.dto.StockUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StockConsumerService {

    private static final Logger log = LoggerFactory.getLogger(StockConsumerService.class);

    private static final String STOCK_TOPIC = "stock-topic";

    @KafkaListener(topics = STOCK_TOPIC)
    public void consumeStockUpdate(StockUpdate message) {
        log.info("Internal WMS Module: Received stock update for product {}: new quantity is {}",
                 message.productId(),
                 message.quantity());
    }
}
