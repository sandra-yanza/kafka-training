package co.example.kafkatraining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class KafkaTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaTrainingApplication.class, args);
    }

}
