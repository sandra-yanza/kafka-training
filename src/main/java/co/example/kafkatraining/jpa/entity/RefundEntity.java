package co.example.kafkatraining.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundEntity {

    @Id
    private String refundId;
    private String customerId;
    private double amount;
    private LocalDate refundDate;

    public void incrementAmount(double amount) {
        this.amount += amount;
    }
}
