package com.vendor_marketplace.payment_service.kafka.publisher.Impl;

import com.vendor_marketplace.common.dto.event.ProductUpdateStockEvent;
import com.vendor_marketplace.common.dto.event.SellerReportCreateEvent;
import com.vendor_marketplace.common.dto.event.TransactionCreateEvent;
import com.vendor_marketplace.payment_service.kafka.publisher.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
class KafkaEventPublisherImpl implements KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
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

    @Override
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

    @Override
    public void publishPaymentSuccessProductUpdateStockEvent(ProductUpdateStockEvent event) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(PAYMENT_SUCCESS_PRODUCT_UPDATE_STOCK_TOPIC, event.getProductId(), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish payment success product update stock event", ex);
            } else {
                log.info("Payment success product update stock event published successfully | topic={} | partition={} | offset={}",
                        PAYMENT_SUCCESS_PRODUCT_UPDATE_STOCK_TOPIC, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });
    }

}
