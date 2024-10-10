package co.example.kafkatraining.consumers;

import co.example.kafkatraining.handler.SaleHandler;
import co.example.kafkatraining.schemas.Sale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalesConsumer {

    private final SaleHandler handler;

    @KafkaListener(id = "SALES", topics = "SALES", errorHandler = "simpleErrorHandler")
    public void consume(@Header(KafkaHeaders.RECEIVED_KEY) String key,
                        Sale message) {

        log.info("Consuming sale {} with key {}", message, key);

        handler.process(message);
    }


    // Error handler que simplemente registra el error
    public ConsumerAwareListenerErrorHandler simpleErrorHandler() {
        return (message, exception, consumer) -> {
            log.error("Error processing message: {}", message.getPayload());
            log.error("Exception: ", exception);
            return null; // Continuar sin ninguna otra acción
        };
    }
}
