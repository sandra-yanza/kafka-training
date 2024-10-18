package co.example.kafkatraining.producers;

import co.example.kafkatraining.schemas.LowStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Component
@RequiredArgsConstructor
@Slf4j
public class LowStockProducer {

    private static final String TOPIC_NAME = "INVENTORY_ALERTS";

    private final KafkaTemplate<String, LowStock> kafkaTemplate;

    public void send(LowStock message) {

        kafkaTemplate.send(TOPIC_NAME, message.id(), message);

    }


}
