package co.example.kafkatraining.model;

import java.time.LocalDate;
import java.util.List;

public record Sale(String saleId, String customerId, double amount, LocalDate saleDate, List<Item> items) {
}

