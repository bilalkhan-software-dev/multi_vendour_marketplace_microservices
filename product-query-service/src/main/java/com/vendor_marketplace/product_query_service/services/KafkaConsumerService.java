package com.vendor_marketplace.product_query_service.services;

import com.vendor_marketplace.common.dto.event.ProductCreateEvent;
import com.vendor_marketplace.common.dto.event.ProductDeleteEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateStockEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ProductConsumerService productConsumerService;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2.0, maxDelay = 30000),
            numPartitions = "3",
            exclude = IllegalArgumentException.class
    )
    @KafkaListener(
            topics = PRODUCT_CREATE_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeProductCreateEvent(
            @Payload ProductCreateEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment ack) {

        log.info("Received ProductCreateEvent | key={} | partition={} | offset={} | productId={} | sellerId={}",
                key, partition, offset, event.getProductId(), event.getSellerId());

        try {
            productConsumerService.addProduct(event);
            ack.acknowledge();
            log.info("ProductCreateEvent processed successfully | productId={}", event.getProductId());
        } catch (IllegalArgumentException e) {
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing ProductCreateEvent | productId={} | sellerId={}",
                    event.getProductId(), event.getSellerId(), e);
            throw e; // trigger retry
        }
    }

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2.0, maxDelay = 30000),
            numPartitions = "3",
            exclude = IllegalArgumentException.class

    )
    @KafkaListener(
            topics = PRODUCT_UPDATE_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeProductUpdateEvent(
            @Payload ProductUpdateEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment ack) {

        log.info("Received ProductUpdateEvent | key={} | partition={} | offset={} | productId={}",
                key, partition, offset, event.getProductId());

        try {
            productConsumerService.updateProduct(event);
            ack.acknowledge();
            log.info("ProductUpdateEvent processed successfully | productId={}", event.getProductId());
        } catch (IllegalArgumentException e) {
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing ProductUpdateEvent | productId={}", event.getProductId(), e);
            throw e; // trigger retry
        }
    }

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2.0, maxDelay = 30000),
            numPartitions = "3",
            exclude = IllegalArgumentException.class

    )
    @KafkaListener(
            topics = PRODUCT_DELETE_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeProductDeleteEvent(
            @Payload ProductDeleteEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment ack) {

        log.info("Received ProductDeleteEvent | key={} | partition={} | offset={} | productId={}",
                key, partition, offset, event.getProductId());

        try {
            productConsumerService.deleteProductById(event);
            ack.acknowledge();
            log.info("ProductDeleteEvent processed successfully | productId={}", event.getProductId());
        } catch (IllegalArgumentException e) {
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing ProductDeleteEvent | productId={}", event.getProductId(), e);
            throw e; // trigger retry
        }
    }

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2.0, maxDelay = 30000),
            numPartitions = "3",
            exclude = IllegalArgumentException.class
    )
    @KafkaListener(
            topics = PRODUCT_UPDATE_STOCK_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeProductUpdateStockEvent(
            @Payload ProductUpdateStockEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment ack) {

        log.info("Received ProductUpdateStockEvent | key={} | partition={} | offset={} | productId={} | quantity={}",
                key, partition, offset, event.getProductId(), event.getQuantity());

        try {
            productConsumerService.updateStocks(event);
            ack.acknowledge();
            log.info("ProductUpdateStockEvent processed successfully | productId={}", event.getProductId());
        } catch (IllegalArgumentException e) {
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing ProductUpdateStockEvent | productId={}", event.getProductId(), e);
            throw e; // trigger retry
        }
    }

    @DltHandler
    public void handleDLT(
            Object event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset) {

        log.error("DLT received event | key={} | partition={} | offset={} | event={}",
                key, partition, offset, event);
        String payloadType = event.getClass().getSimpleName();
        try {
            if (event instanceof ProductCreateEvent) {
                log.info("Failed ProductCreateEvent: {}", event);
            } else if (event instanceof ProductUpdateEvent) {
                log.info("Failed ProductUpdateEvent: {}", event);
            } else if (event instanceof ProductDeleteEvent) {
                log.info("Failed ProductDeleteEvent: {}", event);
            } else if (event instanceof ProductUpdateStockEvent) {
                log.info("Failed ProductUpdateStockEvent: {}", event);
            } else {
                log.warn("Unknown event type in DLT: {}", payloadType);
            }
        } catch (Exception e) {
            log.error("Error handling DLT event | key={} | event={}", key, event, e);
        }
    }
}
