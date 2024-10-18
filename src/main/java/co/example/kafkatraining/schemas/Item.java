package co.example.kafkatraining.schemas;

public record Item (String id,
                    int quantity,
                    double value) {
}
