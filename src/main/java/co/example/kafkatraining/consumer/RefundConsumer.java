package co.example.kafkatraining.consumer;

import co.example.kafkatraining.handler.RefundHandler;
import co.example.kafkatraining.schemas.Refund;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RefundConsumer {

    private final RefundHandler handler;

    @KafkaListener(id = "REFUND", topics = "REFUND", groupId = "refund-group", containerFactory = "refundKafkaListenerContainerFactory")
    public void consume(Refund refund) {
        log.info("Starting consume refund.");

        try {
            handler.process(refund);
            log.info("Refund processed with success.");
        } catch (Exception e) {
            log.error("Error in consume refund. Description: {}", e.getMessage());
        }

    }






}
