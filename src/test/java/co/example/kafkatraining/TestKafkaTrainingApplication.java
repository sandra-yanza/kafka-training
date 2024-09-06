package co.example.kafkatraining;

import org.springframework.boot.SpringApplication;

public class TestKafkaTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.from(KafkaTrainingApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
