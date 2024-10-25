package co.example.kafkatraining.schemas;

import java.time.LocalDate;
import java.util.List;

public record Refund(String refundId,
                     String customerId,
                     double amount,
                     LocalDate refundDate,
                     List<Item> items) {
}
