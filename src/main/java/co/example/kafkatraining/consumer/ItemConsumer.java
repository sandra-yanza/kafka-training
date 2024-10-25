package co.example.kafkatraining.consumer;

import co.example.kafkatraining.handler.ItemHandler;
import co.example.kafkatraining.schemas.ItemManagement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemConsumer {

    private final ItemHandler handler;

    @KafkaListener(id = "ITEM", topics = "ITEM", groupId = "item-group", containerFactory = "itemManagementKafkaListenerContainerFactory")
    public void consume(ItemManagement itemManagement) {
        log.info("Starting consume itemManagement.");

        try {
            handler.process(itemManagement);
            log.info("ItemManagement processed with success.");
        } catch (Exception e) {
            log.error("Error in consume ItemManagement. Description: {}", e.getMessage());
        }

    }






}
