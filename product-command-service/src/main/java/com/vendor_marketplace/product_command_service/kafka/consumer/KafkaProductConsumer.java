package com.vendor_marketplace.product_command_service.kafka.consumer;

import com.vendor_marketplace.common.dto.event.ProductUpdateStockEvent;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.product_command_service.services.ProductService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.ORDER_CONFIRMED_CANCEL_PRODUCT_UPDATE_STOCK_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProductConsumer {

    private final ProductService productService;

    // Consume order cancel, confirm update stock event
    @RetryableTopic(
            attempts = "2",
            backoff = @Backoff(delay = 4000, multiplier = 2.0, maxDelay = 15000),
            numPartitions = "3",
            exclude = {ValidationException.class, ResourceNotFoundException.class}
    )
    @KafkaListener(
            topics = ORDER_CONFIRMED_CANCEL_PRODUCT_UPDATE_STOCK_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeOrderCancelUpdateStockEvent(
            @Payload ProductUpdateStockEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment ack
    ) {
        log.info("Received orderCancelProductUpdateStockEvent | key={} | partition={} | offset={} | productId={} | quantity={}",
                key, partition, offset, event.getProductId(), event.getQuantity());

        try {
            productService.updateStocks(event.getProductId(), event.getQuantity());
            ack.acknowledge();
            log.info("orderCancelProductUpdateStockEvent processed successfully | productId={}", event.getProductId());
        } catch (Exception e) {
            log.error("Error processing orderCancelProductUpdateStockEvent | productId={}", event.getProductId(), e);
            throw e;
        }
    }


}
