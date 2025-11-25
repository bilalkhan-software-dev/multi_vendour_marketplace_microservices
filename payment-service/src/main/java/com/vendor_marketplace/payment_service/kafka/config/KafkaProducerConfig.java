package com.vendor_marketplace.payment_service.kafka.config;

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
    public NewTopic paymentSuccessTopic() {
        log.info("Payment Success topic created: {}", PAYMENT_SUCCESS_TOPIC);
        return new NewTopic(PAYMENT_SUCCESS_TOPIC, 3, (short) 1);
    }

    @Bean
    public NewTopic paymentFailedTopic() {
        log.info("Payment Failed topic created: {}", PAYMENT_FAILED_TOPIC);
        return new NewTopic(PAYMENT_FAILED_TOPIC, 3, (short) 1);
    }


    @Bean
    public NewTopic transactionTopic() {
        log.info("Transaction topic created: {}", TRANSACTION_CREATED_TOPIC);
        return new NewTopic(TRANSACTION_CREATED_TOPIC, 3, (short) 1);
    }

    @Bean
    public NewTopic sellerReportTopic() {
        log.info("Seller Report topic created: {}", SELLER_REPORT_TOPIC);
        return new NewTopic(SELLER_REPORT_TOPIC, 3, (short) 1);
    }


    @Bean
    public NewTopic paymentSuccessProductUpdateStockTopic() {
        log.info("Payment success PRODUCT UPDATE STOCK topic created: {}", PAYMENT_SUCCESS_PRODUCT_UPDATE_STOCK_TOPIC);
        return new NewTopic(PAYMENT_SUCCESS_PRODUCT_UPDATE_STOCK_TOPIC, 3, (short) 1);
    }





}