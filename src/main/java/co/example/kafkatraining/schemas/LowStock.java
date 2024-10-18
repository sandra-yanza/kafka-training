package co.example.kafkatraining.schemas;

import lombok.Builder;

@Builder
public record LowStock(String id, String saleId, String customerId, String descripcion) {
}
