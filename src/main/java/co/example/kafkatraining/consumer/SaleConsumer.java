package co.example.kafkatraining.consumer;

import co.example.kafkatraining.handler.SalesHandler;
import co.example.kafkatraining.schemas.Sale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SaleConsumer {

    private final SalesHandler handler;


    @KafkaListener(id = "SALES", topics = "SALES")
    public void consume(Sale message) {

        handler.process(message);

    }






}
