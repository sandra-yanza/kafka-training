package co.example.kafkatraining.handler;

import co.example.kafkatraining.jpa.entity.ItemEntity;
import co.example.kafkatraining.jpa.repository.ItemRepository;
import co.example.kafkatraining.producers.HighStockProducer;
import co.example.kafkatraining.producers.InsufficientStockProducer;
import co.example.kafkatraining.producers.LowStockProducer;
import co.example.kafkatraining.schemas.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemHandler {

    private static final int EXCESS_INVENTORY_QUANTITY = 200;
    private static final int LOW_INVENTORY_QUANTITY = 100;
    private final ItemRepository itemRepository;
    private final HighStockProducer highStockProducer;
    private final LowStockProducer lowStockProducer;
    private final InsufficientStockProducer insufficientStockProducer;

    public void process(ItemManagement itemManagement) throws Exception {
        log.info("Starting process ItemManagement.");

        try {

            validateItem(itemManagement);

            switch (itemManagement.typeOperation()) {
                case "CREATE":
                    createItem(itemManagement);
                    validateInventoryAlerts(itemManagement);
                    break;
                case "MODIFY":
                    modifyItem(itemManagement);
                    validateInventoryAlerts(itemManagement);
                    break;
                case "DELETE":
                    deleteItem(itemManagement);
                    break;
                default:
                    log.warn("Type operation not defined for itemManagement.");

            }

        } catch (Exception e) {
            log.error("Error process ItemManagement.");
            throw e;
        }
    }

    @Transactional
    private void deleteItem(ItemManagement itemManagement) {

        try {
            Optional<ItemEntity> entityOpt = itemRepository.findById(itemManagement.id());

            if (entityOpt.isPresent()) {
                itemRepository.delete(entityOpt.get());
                log.info("Item deleted with Id: {}.", itemManagement.id());
            } else {
                log.warn("Item not found in deleteItem. Id: {}", itemManagement.id());
            }
        } catch (Exception e) {
            log.error("Error process deleteItem.");
            throw e;
        }

    }

    @Transactional
    private void modifyItem(ItemManagement itemManagement) throws Exception {

        try {
            Optional<ItemEntity> entityOpt = itemRepository.findById(itemManagement.id());

            if (entityOpt.isPresent()) {
                ItemEntity itemEntity = entityOpt.get();
                itemEntity.setQuantity(itemManagement.quantity());
                itemEntity.setName(itemManagement.name());
                itemEntity.setPrice(itemManagement.price());

                itemRepository.save(itemEntity);
                log.info("Item modified with Id: {}.", itemManagement.id());
            } else {
                log.warn("Item not found. Id: {}", itemManagement.id());
                throw new Exception("Item not found. Id: " + itemManagement.id());
            }
        } catch (Exception e) {
            log.error("Error process modifyItem.");
            throw e;
        }

    }

    @Transactional
    private void createItem(ItemManagement itemManagement) {

        try {
            idempotentItem(itemManagement);

            ItemEntity itemNew = ItemEntity.builder()
                    .itemId(itemManagement.id())
                    .quantity(itemManagement.quantity())
                    .name(itemManagement.name())
                    .price(itemManagement.price())
                    .build();

            itemRepository.save(itemNew);
            log.info("Item created with Id: {}.", itemManagement.id());
        } catch (Exception e) {
            log.error("Error process createItem.");
            throw e;
        }


    }

    private void validateItem(ItemManagement itemManagement) {
        if (itemManagement == null || itemManagement.id() == null || itemManagement.name() == null || itemManagement.price() == null || itemManagement.quantity() == null) {
            throw new IllegalArgumentException("Invalid entry data of ItemManagement.");
        }
        if(itemManagement.quantity() < 0) {
            throw new IllegalArgumentException("Invalid quantity for the item. Quantity cannot be 0 or less than 0. Id: " + itemManagement.id());
        }
    }

    private void idempotentItem(ItemManagement itemManagement) {
        if (itemRepository.existsById(itemManagement.id())) {
            try {
                throw new Exception("Item already exists. Id: " + itemManagement.id());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void validateInventoryAlerts(ItemManagement itemManagement) {

        if (itemManagement.quantity() < 0) {
            log.info("Inventory insufficient stock for item: {}.", itemManagement.id());
            InsufficientStock message = InsufficientStock.builder()
                    .id(itemManagement.id())
                    .saleId("0")
                    .customerId("0")
                    .descripcion("Inventory insufficient stock for item %s with %s quantity".formatted(itemManagement.id(), itemManagement.quantity()))
                    .build();

            insufficientStockProducer.send(message);

        } else if (itemManagement.quantity() < LOW_INVENTORY_QUANTITY) {
            log.info("Inventory near out of stock for item: {}.", itemManagement.id());
            LowStock message = LowStock.builder()
                    .id(itemManagement.id())
                    .saleId("0")
                    .customerId("0")
                    .descripcion("Inventory near out of stock for item %s with %s quantity".formatted(itemManagement.id(), itemManagement.quantity()))
                    .build();

            lowStockProducer.send(message);

        } else if (itemManagement.quantity() > EXCESS_INVENTORY_QUANTITY) {
            log.info("Excess inventory quantity for item: {}.", itemManagement.id());
            HighStock message = HighStock.builder()
                    .id(itemManagement.id())
                    .customerId("0")
                    .descripcion("Excess inventory for item %s with quantity %s".formatted(itemManagement.id(), itemManagement.quantity()))
                    .build();

            highStockProducer.send(message);
        }

    }

}
