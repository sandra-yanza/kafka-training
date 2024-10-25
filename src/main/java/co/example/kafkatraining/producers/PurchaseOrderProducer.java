package co.example.kafkatraining.producers;

import co.example.kafkatraining.schemas.PurchaseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class PurchaseOrderProducer {

    private static final String TOPIC_NAME = "PURCHASE_ORDER";

    private final KafkaTemplate<String, PurchaseOrder> kafkaTemplate;

    public void send(PurchaseOrder purchaseOrder) {

        kafkaTemplate.send(TOPIC_NAME, purchaseOrder.idOrder(), purchaseOrder);

    }


}
