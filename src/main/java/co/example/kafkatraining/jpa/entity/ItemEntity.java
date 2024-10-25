package co.example.kafkatraining.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity {

    @Id
    private String itemId;
    private int quantity;
    private String name;
    private double price;


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
