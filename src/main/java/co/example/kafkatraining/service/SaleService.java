package co.example.kafkatraining.service;

import co.example.kafkatraining.jpa.entity.ItemEntity;
import co.example.kafkatraining.jpa.repository.ItemRepository;
import co.example.kafkatraining.model.InsufficientStock;
import co.example.kafkatraining.model.LowStock;
import co.example.kafkatraining.model.Item;
import co.example.kafkatraining.model.Sale;
import co.example.kafkatraining.producers.InsufficientStockProducer;
import co.example.kafkatraining.producers.LowStockProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaleService {

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
                    InsufficientStock message = InsufficientStock.builder()
                            .id(item.id())
                            .saleId(sale.saleId())
                            .customerId(sale.customerId())
                            .descripcion("Inventory insufficient stock for sale with %s quantity".formatted(itemEntity.getQuantity()))
                            .build();

                    insufficientStockProducer.send(message);
                }


                repository.save(itemEntity);


                if (itemEntity.getQuantity() < 100 ){
                    LowStock message = LowStock.builder()
                            .id(item.id())
                            .saleId(sale.saleId())
                            .customerId(sale.customerId())
                            .descripcion("Inventory near out of stock %s".formatted(itemEntity.getQuantity()))
                            .build();

                    lowStockProducer.send(message);
                }


            }
        }
    }
}
