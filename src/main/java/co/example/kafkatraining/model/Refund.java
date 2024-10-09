package co.example.kafkatraining.model;

import java.time.LocalDate;
import java.util.List;

public record Refund(String refundId,
                     String saleId,
                     double refundAmount,
                     LocalDate refundDate,
                     String refundReason,
                     List<Item> items) {
}

