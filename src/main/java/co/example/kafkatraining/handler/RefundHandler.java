package co.example.kafkatraining.handler;

import co.example.kafkatraining.jpa.entity.ItemEntity;
import co.example.kafkatraining.jpa.entity.RefundEntity;
import co.example.kafkatraining.jpa.repository.ItemRepository;
import co.example.kafkatraining.jpa.repository.RefundRepository;
import co.example.kafkatraining.producers.HighStockProducer;
import co.example.kafkatraining.schemas.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundHandler {

    private static final int EXCESS_INVENTORY_QUANTITY = 200;
    private final ItemRepository itemRepository;
    private final RefundRepository refundRepository;
    private final HighStockProducer highStockProducer;

    @Transactional
    public void process(Refund refund) {
        log.info("Starting process refund.");

        try {

            validateRefund(refund);

            idempotentRefund(refund);

            double amountRefund = 0;
            for (Item item : refund.items()) {
                log.info("item: {}", item.toString());

                Optional<ItemEntity> entityOpt = itemRepository.findById(item.id());

                //Verificar que el pedido existe y es elegible para reembolso
                if (entityOpt.isPresent()) {

                    ItemEntity itemEntity = entityOpt.get();

                    //Actualizar el inventario si es necesario
                    itemEntity.incrementQuantity(item.quantity());

                    itemRepository.save(itemEntity);

                    if (itemEntity.getQuantity() > EXCESS_INVENTORY_QUANTITY) {
                        log.info("Excess inventory quantity for item: {}.", itemEntity.getItemId());
                        HighStock highStock = HighStock.builder()
                                .id(item.id())
                                .customerId(refund.customerId())
                                .descripcion("Excess inventory for item %s with quantity %s".formatted(itemEntity.getItemId(), itemEntity.getQuantity()))
                                .build();

                        highStockProducer.send(highStock);
                    }

                    //Calcular el monto correcto a reembolsar
                    amountRefund += item.quantity() * item.value();

                } else {
                    log.warn("Item not found. Id: {}", item.id());
                }
            }

            RefundEntity refundEntity = RefundEntity.builder()
                    .refundId(refund.refundId())
                    .customerId(refund.customerId())
                    .amount(amountRefund)
                    .refundDate(refund.refundDate())
                    .build();

            //Registrar el reembolso en el sistema
            refundRepository.save(refundEntity);

        } catch (Exception e) {
            log.error("Error process refund.");
            throw e;
        }
    }

    private void validateRefund(Refund refund) {
        if(refund == null || refund.refundId() == null || refund.items() == null) {
            throw new IllegalArgumentException("Invalid entry data of Refund.");
        }
    }

    private void idempotentRefund(Refund refund) {
        if(refundRepository.existsById(refund.refundId())) {
            try {
                throw new Exception("Refund already exists. Id: " + refund.refundId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
