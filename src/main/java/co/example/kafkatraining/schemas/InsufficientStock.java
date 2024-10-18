package co.example.kafkatraining.schemas;

import lombok.Builder;

@Builder
public record InsufficientStock(String id, String saleId, String customerId, String descripcion) {
}
