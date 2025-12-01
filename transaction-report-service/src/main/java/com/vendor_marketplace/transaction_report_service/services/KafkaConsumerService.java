package com.vendor_marketplace.transaction_report_service.services;

import com.vendor_marketplace.common.dto.event.*;
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

import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final SellerReportService sellerReportService;
    private final TransactionService transactionService;

    @RetryableTopic(
            attempts = "2",
            exclude = ValidationException.class,
            backOff = @BackOff(delay = 5000, multiplier = 2.0, maxDelay = 30000),
            numPartitions = "3"
    )
    @KafkaListener(topics = TRANSACTION_CREATED_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    void consumeTransactionCreatedEvent(
            @Payload TransactionCreateEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack
    ) {


        log.info("Received TransactionCreateEvent | key={} | partition={} | offset={} | sellerId={}",
                key, partition, offset, event.getSellerId());

        try {
            transactionService.savaTransaction(event);
            ack.acknowledge();
            log.info("TransactionCreateEvent processed successfully");
        } catch (ValidationException e) {
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing TransactionCreateEvent | event={}", event, e);
            throw e; // trigger retry
        }
    }

    @RetryableTopic(
            attempts = "2",
            exclude = ValidationException.class,
            backOff = @BackOff(delay = 5000, multiplier = 2.0, maxDelay = 30000),
            numPartitions = "3"
    )
    @KafkaListener(topics = SELLER_REPORT_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    void consumeSellerReportCreatedEvent(
            @Payload SellerReportCreateEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack
    ) {
        log.info("Received SellerReportEvent | key={} | partition={} | offset={} | sellerId={}",
                key, partition, offset, event.getSellerId());

        try {
            sellerReportService.saveReportWithMonthlyReset(event);
            ack.acknowledge();
            log.info("SellerReportEvent processed successfully");
        } catch (ValidationException e) {
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing SellerReportEvent | event={}", event, e);
            throw e; // trigger retry
        }

    }

    @DltHandler
    public void handleDLT(
            @Payload Object event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,Acknowledgment ack) {

        log.error("DLT received event | key={} | partition={} | offset={} | event={}",
                key, partition, offset, event);
        String payloadType = event.getClass().getSimpleName();

        if (event instanceof TransactionCreateEvent) {
            log.info("Failed TransactionCreateEvent: {}", event);
        } else if (event instanceof SellerReportCreateEvent) {
            log.info("Failed SellerReportEvent: {}", event);
        } else {
            log.warn("Unknown event type in DLT: {}", payloadType);
        }

        ack.acknowledge();

    }


}
