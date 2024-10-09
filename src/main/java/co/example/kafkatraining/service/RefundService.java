package co.example.kafkatraining.service;

import co.example.kafkatraining.jpa.entity.ItemEntity;
import co.example.kafkatraining.jpa.repository.ItemRepository;
import co.example.kafkatraining.model.InsufficientStock;
import co.example.kafkatraining.model.InventoryAlert;
import co.example.kafkatraining.model.Item;
import co.example.kafkatraining.model.Refund;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final ItemRepository repository;

    public void process(Refund refund) {

        for (Item item : refund.items()) {

            Optional<ItemEntity> entityOpt = repository.findById(item.id());

            if (entityOpt.isPresent()) {

                ItemEntity itemEntity = entityOpt.get();

                itemEntity.incrementQuantity(item.quantity());

                repository.save(itemEntity);

            }
        }
    }
}
