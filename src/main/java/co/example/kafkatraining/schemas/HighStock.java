package co.example.kafkatraining.schemas;

import lombok.Builder;

@Builder
public record HighStock(String id, String customerId, String descripcion) {
}
