package co.example.kafkatraining.config;

import co.example.kafkatraining.schemas.ItemManagement;
import co.example.kafkatraining.schemas.Refund;
import co.example.kafkatraining.schemas.Sale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Sale> saleKafkaListenerContainerFactory(
            ConsumerFactory<String, Sale> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Sale> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Refund> refundKafkaListenerContainerFactory(
            ConsumerFactory<String, Refund> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Refund> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ItemManagement> itemManagementKafkaListenerContainerFactory(
            ConsumerFactory<String, ItemManagement> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, ItemManagement> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Sale> saleConsumerFactory() {
        Map<String, Object> props = new HashMap<>(KafkaConsumerConfig.commonConsumerProperties());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "co.example.kafkatraining.schemas.Sale");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, Refund> refundConsumerFactory() {
        Map<String, Object> props = new HashMap<>(KafkaConsumerConfig.commonConsumerProperties());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "co.example.kafkatraining.schemas.Refund");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, ItemManagement> itemManagementConsumerFactory() {
        Map<String, Object> props = new HashMap<>(KafkaConsumerConfig.commonConsumerProperties());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "co.example.kafkatraining.schemas.ItemManagement");
        return new DefaultKafkaConsumerFactory<>(props);
    }


    private static Map<String, Object> commonConsumerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

}


