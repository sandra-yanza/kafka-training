package co.example.kafkatraining.model;

public record Item(
        String id,
        int quantity,
        double value) {
}
