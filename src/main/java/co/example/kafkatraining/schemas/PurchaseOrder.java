package co.example.kafkatraining.schemas;

import lombok.Builder;

@Builder
public record PurchaseOrder(String idOrder,
                            String idItem,
                            int quantity,
                            double value) {
}
