package co.example.kafkatraining.jpa.repository;

import co.example.kafkatraining.jpa.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, String> {
}
