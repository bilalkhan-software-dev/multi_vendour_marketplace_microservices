package com.vendor_marketplace.order_service.kafka.config;

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
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

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
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        configProps.put("spring.json.trusted.packages", "com.vendor_marketplace.common.dto.event");
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic orderTopic() {
        log.info("Order topic created: {}", ORDER_CREATED_TOPIC);
        return new NewTopic(ORDER_CREATED_TOPIC, 3, (short) 1);
    }

    @Bean
    public NewTopic sellerReportTopic() {
        log.info("Seller Report topic created: {}", SELLER_REPORT_TOPIC);
        return new NewTopic(SELLER_REPORT_TOPIC, 3, (short) 1);
    }


    @Bean
    public NewTopic orderNotificationTopic() {
        log.info("Order notification topic created: {}", ORDER_NOTIFICATION_TOPIC);
        return new NewTopic(ORDER_NOTIFICATION_TOPIC, 3, (short) 1);
    }

    @Bean
    public NewTopic orderCancelProductStockTopic() {
        log.info("Order cancel product update stock topic created: {}", ORDER_CANCEL_PRODUCT_UPDATE_STOCK_TOPIC);
        return new NewTopic(ORDER_CANCEL_PRODUCT_UPDATE_STOCK_TOPIC, 3, (short) 1);
    }


}