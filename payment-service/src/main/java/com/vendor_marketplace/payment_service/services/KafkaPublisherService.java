package com.vendor_marketplace.payment_service.services;

import com.vendor_marketplace.common.dto.event.SellerReportCreateEvent;
import com.vendor_marketplace.common.dto.event.TransactionCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.SELLER_REPORT_TOPIC;
import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.TRANSACTION_CREATED_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaPublisherService {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void publishTransactionEvent(TransactionCreateEvent event) {

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TRANSACTION_CREATED_TOPIC, event.getSellerId(), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish transaction create event", ex);
            } else {
                log.info("Transaction created event published successfully | topic={} | partition={} | offset={} sellerId: {}",
                        TRANSACTION_CREATED_TOPIC, result.getRecordMetadata().partition(), result.getRecordMetadata().offset(), event.getSellerId());
            }
        });

    }

    public void publishSellerReportEvent(SellerReportCreateEvent event) {

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(SELLER_REPORT_TOPIC, event.getSellerId(), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish seller report event", ex);
            } else {
                log.info("Seller report event published successfully | topic={} | partition={} | offset={}",
                        SELLER_REPORT_TOPIC, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });


    }

}
