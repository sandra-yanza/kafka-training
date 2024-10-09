package co.example.kafkatraining.producers;

import co.example.kafkatraining.model.LowStock;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Component
@RequiredArgsConstructor
public class LowStockProducer {

    private static final String TOPIC_NAME = "inventory_alerts";

    private final KafkaTemplate<String, LowStock> kafkaTemplate;

    public void send(LowStock message) {


        CompletableFuture<SendResult<String, LowStock>> result = kafkaTemplate.send(TOPIC_NAME, message.id(), message);

        result.thenAccept((insufficientStockSendResult) -> {
            System.out.println("Sent sample message [" + message + "] to " + TOPIC_NAME);
        });

        result.exceptionally(ex -> {
            System.err.println("Error al enviar el mensaje: " + ex.getMessage());
            return null;
        });

    }


}
