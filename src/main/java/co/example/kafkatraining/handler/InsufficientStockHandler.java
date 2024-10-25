package co.example.kafkatraining.handler;

import co.example.kafkatraining.jpa.entity.ItemEntity;
import co.example.kafkatraining.jpa.repository.ItemRepository;
import co.example.kafkatraining.producers.PurchaseOrderProducer;
import co.example.kafkatraining.schemas.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsufficientStockHandler {

    private static final int PURCHASE_ORDER_QUANTITY = 150;
    private final ItemRepository itemRepository;
    private final PurchaseOrderProducer purchaseOrderProducer;

    @Transactional
    public void process(InsufficientStock insufficientStock) throws Exception {
        log.info("Starting process InsufficientStock.");
        double amountOrder = 0;

        try {

            Optional<ItemEntity> entityOpt = itemRepository.findById(insufficientStock.id());

            //Validar que los artículos solicitados estén disponibles
            if (entityOpt.isPresent()) {

                ItemEntity itemEntity = entityOpt.get();

                //Calcular el total de la orden correctamente
                amountOrder = itemEntity.getPrice() * PURCHASE_ORDER_QUANTITY;

                //Generar un número de orden único
                PurchaseOrder messageOrder = PurchaseOrder.builder()
                        .idOrder(UUID.randomUUID().toString())
                        .idItem(insufficientStock.id())
                        .quantity(PURCHASE_ORDER_QUANTITY)
                        .value(amountOrder)
                        .build();

                //Enviar la order de compra al tópico
                purchaseOrderProducer.send(messageOrder);

                //Actualizar el inventario al procesar la orden
                itemEntity.incrementQuantity(PURCHASE_ORDER_QUANTITY);
                itemRepository.save(itemEntity);

            } else {
                log.warn("Item not found. Id: {}", insufficientStock.id());
            }

        } catch (Exception e) {
            log.error("Error process InsufficientStock.");
            throw e;
        }
    }

}
