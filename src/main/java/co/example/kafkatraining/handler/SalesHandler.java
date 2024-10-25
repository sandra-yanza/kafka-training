package co.example.kafkatraining.handler;

import co.example.kafkatraining.jpa.entity.ItemEntity;
import co.example.kafkatraining.jpa.repository.ItemRepository;
import co.example.kafkatraining.schemas.InsufficientStock;
import co.example.kafkatraining.schemas.LowStock;
import co.example.kafkatraining.schemas.Item;
import co.example.kafkatraining.schemas.Sale;
import co.example.kafkatraining.producers.InsufficientStockProducer;
import co.example.kafkatraining.producers.LowStockProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesHandler {

    private static final int LOW_INVENTORY_QUANTITY = 100;

    private final ItemRepository repository;
    private final LowStockProducer lowStockProducer;
    private final InsufficientStockProducer insufficientStockProducer;


    public void process(Sale sale) {

        for (Item item: sale.items()){

            Optional<ItemEntity> entityOpt = repository.findById(item.id());

            if (entityOpt.isPresent()) {

                ItemEntity itemEntity = entityOpt.get();

                try{

                    itemEntity.decreaseQuantity(item.quantity());

                } catch (Exception e) {
                    InsufficientStock message2 = InsufficientStock.builder()
                            .id(item.id())
                            .saleId(sale.saleId())
                            .customerId(sale.customerId())
                            .descripcion("Inventory insufficient stock for sale with %s quantity".formatted(itemEntity.getQuantity()))
                            .build();

                    insufficientStockProducer.send(message2);
                }


                repository.save(itemEntity);


                if (itemEntity.getQuantity() < LOW_INVENTORY_QUANTITY ){
                    LowStock message5 = LowStock.builder()
                            .id(item.id())
                            .saleId(sale.saleId())
                            .customerId(sale.customerId())
                            .descripcion("Inventory near out of stock %s".formatted(itemEntity.getQuantity()))
                            .build();

                    lowStockProducer.send(message5);
                }


            }
        }
    }
}
