package com.vendor_marketplace.product_command_service.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.*;


@Configuration
@Slf4j
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put("spring.json.trusted.packages", "com.vendor_marketplace.common.dto.event");
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        KafkaTemplate<String, Object> template = new KafkaTemplate<>(producerFactory());
        return template;
    }

    @Bean
    public NewTopic productCreateTopic() {
        log.info("Product create topic created: {}", PRODUCT_CREATE_TOPIC);
        return new NewTopic(PRODUCT_CREATE_TOPIC, 3, (short) 1);
    }
    @Bean
    public NewTopic productUpdateTopic() {
        log.info("Product update topic created: {}", PRODUCT_UPDATE_TOPIC);
        return new NewTopic(PRODUCT_UPDATE_TOPIC, 3, (short) 1);
    }
    @Bean
    public NewTopic productDeleteTopic() {
        log.info("Product delete topic created: {}", PRODUCT_DELETE_TOPIC);
        return new NewTopic(PRODUCT_DELETE_TOPIC, 3, (short) 1);
    }
    @Bean
    public NewTopic productUpdateStockTopic() {
        log.info("Product update stock topic created: {}", PRODUCT_UPDATE_STOCK_TOPIC);
        return new NewTopic(PRODUCT_UPDATE_STOCK_TOPIC, 3, (short) 1);
    }

}