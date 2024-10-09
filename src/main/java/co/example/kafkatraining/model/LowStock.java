package co.example.kafkatraining.model;

import lombok.Builder;

@Builder
public record LowStock(String id, String saleId, String customerId, String descripcion) {
}
