package co.example.kafkatraining.consumers;

import co.example.kafkatraining.model.Refund;
import co.example.kafkatraining.service.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RefundConsumer {

    private final RefundService service;

    @KafkaListener(topics = "refund_events")
    public void processMessage(Refund refund) {

        log.info("Received message {}", refund);

        service.process(refund);

    }
}
