package co.example.kafkatraining.producers;

import co.example.kafkatraining.model.InventoryAlert;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Component
@RequiredArgsConstructor
public class InventoryAlertProducer {

    private static final String TOPIC_NAME = "inventory_alerts";

    private final KafkaTemplate<String, InventoryAlert> kafkaTemplate;

    public void send(InventoryAlert message) {


        CompletableFuture<SendResult<String, InventoryAlert>> result = kafkaTemplate.send(TOPIC_NAME, message.id(), message);

        result.thenAccept((insufficientStockSendResult) -> {
            System.out.println("Sent sample message [" + message + "] to " + TOPIC_NAME);
        });

        result.exceptionally(ex -> {
            System.err.println("Error al enviar el mensaje: " + ex.getMessage());
            return null;
        });

    }


}
