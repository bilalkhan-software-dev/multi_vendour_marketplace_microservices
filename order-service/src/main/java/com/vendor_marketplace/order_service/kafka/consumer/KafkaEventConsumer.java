package com.vendor_marketplace.order_service.kafka.consumer;

import com.vendor_marketplace.common.dto.event.PaymentCancelOrFailEvent;
import com.vendor_marketplace.common.dto.event.PaymentSuccessEvent;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.order_service.kafka.publisher.KafkaPublisherService;
import com.vendor_marketplace.order_service.services.OrderService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.PAYMENT_SUCCESS_TOPIC;
import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.PAYMENT_FAILED_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEventConsumer {


    private final OrderService orderService;


    @RetryableTopic(
            attempts = "2",
            backOff = @BackOff(delay = 4000, multiplier = 2.0, maxDelay = 15000),
            numPartitions = "3",
            exclude = {ResourceNotFoundException.class, ValidationException.class}
    )
    @KafkaListener(
            topics = PAYMENT_SUCCESS_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumePaymentSuccessEvent(
            @Payload PaymentSuccessEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment ack
    ) {
        log.info("Received PaymentSuccessEvent | key={} | partition={} | offset={} | orderId={} | email={}",
                key, partition, offset, event.getOrderId(), event.getEmail());

        try {
            orderService.updateOrderAndPaymentStatus(event.getOrderId(), event.getOrderStatus(), event.getPaymentStatus(), event.getEmail());
            ack.acknowledge();
            log.info("PaymentSuccessEvent processed successfully | orderId={}", event.getOrderId());
        } catch (Exception e) {
            log.error("Error processing PaymentSuccessEvent | orderId={} | email={}",
                    event.getOrderId(), event.getEmail(), e);
            throw e;
        }
    }


    @RetryableTopic(
            attempts = "2",
            backOff = @BackOff(delay = 4000, multiplier = 2.0, maxDelay = 15000),
            numPartitions = "3",
            exclude = {ResourceNotFoundException.class, ValidationException.class}
    )
    @KafkaListener(
            topics = PAYMENT_FAILED_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumePaymentCancelOrFailEvent(
            @Payload PaymentCancelOrFailEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment ack
    ) {
        log.info("Received PaymentCancelOrFailEvent | key={} | partition={} | offset={} | orderId={} | email={}",
                key, partition, offset, event.getOrderId(), event.getEmail());

        try {
            orderService.updateOrderAndPaymentStatus(event.getOrderId(), event.getOrderStatus(), event.getPaymentStatus(), event.getEmail());
            ack.acknowledge();
            log.info("PaymentCancelOrFailEvent processed successfully | orderId={}", event.getOrderId());
        } catch (Exception e) {
            log.error("Error processing PaymentCancelOrFailEvent | orderId={} | email={}",
                    event.getOrderId(), event.getEmail(), e);
            throw e;
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
            if (event instanceof PaymentSuccessEvent) {
                log.info("Failed PaymentSuccessEvent: {}", event);
            } else if (event instanceof PaymentCancelOrFailEvent) {
                log.info("Failed PaymentCancelOrFailEvent: {}", event);
            } else {
                log.warn("Unknown event type in DLT: {}", payloadType);
            }
        } catch (Exception e) {
            log.error("Error handling DLT event | key={} | event={}", key, event, e);
        }
    }
}
