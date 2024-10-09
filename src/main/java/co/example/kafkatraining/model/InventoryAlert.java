package co.example.kafkatraining.model;

import lombok.Builder;

@Builder
public record InventoryAlert(String id, String saleId, String customerId, String descripcion) {
}
