package co.example.kafkatraining.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class ItemEntity {

    @Id
    private String itemId;
    private int quantity;

    public void decreaseQuantity(int quantity) throws Exception {

        this.quantity -= quantity;

        if (this.quantity < 0) {
            throw new Exception("insufficient stock");
        }
    }

    public void incrementQuantity(int quantity) {
        this.quantity += quantity;
    }
}
