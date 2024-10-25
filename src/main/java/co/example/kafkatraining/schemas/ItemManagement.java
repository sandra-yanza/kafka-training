package co.example.kafkatraining.schemas;

public record ItemManagement(String id,
                             Integer quantity,
                             String name,
                             Double price,
                             String typeOperation) {
}


