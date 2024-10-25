package co.example.kafkatraining.consumer;

import co.example.kafkatraining.handler.InsufficientStockHandler;
import co.example.kafkatraining.schemas.InsufficientStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InsufficientStockConsumer {

    private final InsufficientStockHandler handler;

    @KafkaListener(id = "INSUFFICIENT_STOCK", topics = "INSUFFICIENT_STOCK", groupId = "insufficient-stock-group", containerFactory = "insufficientStockKafkaListenerContainerFactory")
    public void consume(InsufficientStock insufficientStock) {
        log.info("Starting consume insufficientStock.");

        try {
            handler.process(insufficientStock);
            log.info("InsufficientStock processed with success.");
        } catch (Exception e) {
            log.error("Error in consume InsufficientStock. Description: {}", e.getMessage());
        }

    }






}
