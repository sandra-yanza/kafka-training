package co.example.kafkatraining.jpa.repository;

import co.example.kafkatraining.jpa.entity.RefundEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<RefundEntity, String> {
}
