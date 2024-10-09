package co.example.kafkatraining.producers;

import co.example.kafkatraining.model.InsufficientStock;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class InsufficientStockProducer {

    private static final String TOPIC_NAME = "insufficient_stock";

    private final KafkaTemplate<String, InsufficientStock> kafkaTemplate;

    public void send(InsufficientStock message) {


        CompletableFuture<SendResult<String, InsufficientStock>> result = kafkaTemplate.send(TOPIC_NAME, message.id(),message);

        result.thenAccept((insufficientStockSendResult)->{
            System.out.println("Sent sample message [" + message + "] to " + TOPIC_NAME);
        });

        result.exceptionally(ex -> {
            System.err.println("Error al enviar el mensaje: " + ex.getMessage());
            return null;
        });

    }

}
