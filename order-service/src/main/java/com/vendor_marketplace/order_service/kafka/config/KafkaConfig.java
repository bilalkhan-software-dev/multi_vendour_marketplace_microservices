package com.vendor_marketplace.order_service.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.HashMap;
import java.util.Map;

import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.*;

@Configuration
@EnableKafkaRetryTopic
@Slf4j
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("kafka-retry-scheduler-");
        scheduler.setDaemon(true);
        scheduler.initialize();
        log.info("TaskScheduler initialized for Kafka RetryTopic");
        return scheduler;
    }

    // MANDATORY: Special KafkaTemplate for RetryTopic infrastructure
    @Bean
    public KafkaTemplate<String, Object> defaultRetryTopicKafkaTemplate() {
        log.info("Creating defaultRetryTopicKafkaTemplate for retry mechanism");
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        log.info("Creating regular kafkaTemplate for business operations");
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        JacksonJsonDeserializer<Object> deserializer = new JacksonJsonDeserializer<>();
        deserializer.addTrustedPackages("com.vendor_marketplace.common.dto.event");
        deserializer.setRemoveTypeHeaders(false);
        deserializer.setUseTypeMapperForKey(false);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        configProps.put("spring.json.trusted.packages", "com.vendor_marketplace.common.dto.event");
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
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
        log.info("Send notification topic created: {}", SEND_NOTIFICATION_TOPIC);
        return new NewTopic(SEND_NOTIFICATION_TOPIC, 3, (short) 1);
    }

    @Bean
    public NewTopic orderConfirmCancelProductStockTopic() {
        log.info("Order confirmed/cancel product update stock topic created: {}", ORDER_CONFIRMED_CANCEL_PRODUCT_UPDATE_STOCK_TOPIC);
        return new NewTopic(ORDER_CONFIRMED_CANCEL_PRODUCT_UPDATE_STOCK_TOPIC, 3, (short) 1);
    }



}