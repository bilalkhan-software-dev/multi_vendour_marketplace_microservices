package com.vendor_marketplace.payment_service.kafka.consumer;

import com.stripe.exception.StripeException;
import com.vendor_marketplace.common.dto.event.*;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.payment_service.exception.PaymentException;
import com.vendor_marketplace.payment_service.services.PaymentService;
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

import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.ORDER_CREATED_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEventConsumer {


    private final PaymentService paymentService;


    @RetryableTopic(
            attempts = "2",
            backOff = @BackOff(delay = 4000, multiplier = 2.0, maxDelay = 15000),
            numPartitions = "3",
            exclude = {PaymentException.class}
    )
    @KafkaListener(
            topics = ORDER_CREATED_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeOrderCreatedEvent(
            @Payload OrderCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment ack
    ) {
        log.info("Received OrderCreatedEvent | key={} | partition={} | offset={} | orderId={} | userId={}",
                key, partition, offset, event.getOrderId(), event.getCustomerId());

        try {
            paymentService.processPaymentCreation(event);
            ack.acknowledge();
            log.info("OrderCreatedEvent processed successfully | orderId={}", event.getOrderId());
        }catch (PaymentException e) {
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing OrderCreatedEvent | orderId={} | userId={}",
                    event.getOrderId(), event.getCustomerId(), e);
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
            if (event instanceof OrderCreatedEvent) {
                log.info("Failed OrderCreatedEvent: {}", event);
            } else {
                log.warn("Unknown event type in DLT: {}", payloadType);
            }
        } catch (Exception e) {
            log.error("Error handling DLT event | key={} | event={}", key, event, e);
        }
    }
}
