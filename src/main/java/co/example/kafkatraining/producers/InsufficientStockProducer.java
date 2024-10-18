package co.example.kafkatraining.producers;

import co.example.kafkatraining.schemas.InsufficientStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class InsufficientStockProducer {

    private static final String TOPIC_NAME = "INSUFFICIENT_STOCK";


    private final KafkaTemplate<String, InsufficientStock> kafkaTemplate;



    public void send(InsufficientStock message) {


        CompletableFuture<SendResult<String, InsufficientStock>> result = kafkaTemplate.send(TOPIC_NAME, message.id(),message);

        result.thenAccept((insufficientStockSendResult)->{
            log.info("Sent sample message [{}] to " + TOPIC_NAME, message);
        });

        result.exceptionally(ex -> {
            log.error("Error al enviar el mensaje: {}", ex.getMessage());
            return null;
        });

    }

}
