package com.vendor_marketplace.order_service.kafka.publisher.Imp;

import com.vendor_marketplace.common.dto.event.OrderCreatedEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateStockEvent;
import com.vendor_marketplace.common.dto.event.SellerReportCreateEvent;
import com.vendor_marketplace.common.dto.event.SendNotificationEvent;
import com.vendor_marketplace.order_service.kafka.publisher.KafkaPublisherService;
import lombok.NonNull;
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
class KafkaPublisherServiceImpl implements KafkaPublisherService {

    private final KafkaTemplate<@NonNull String, @NonNull Object> kafkaTemplate;

    @Override
    public void publishOrderCreatedEvent(OrderCreatedEvent event) {

        CompletableFuture<SendResult<@NonNull String, @NonNull Object>> future = kafkaTemplate.send(ORDER_CREATED_TOPIC, event.getOrderId(), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish order created event", ex);
            } else {
                log.info("Order created event published successfully orderId: {} | topic={} | partition={} | offset={}",
                        ORDER_CREATED_TOPIC, event.getOrderId(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });
    }

    /**
     * When User Cancel or Confirm (Payment Success) Order
     */
    @Override
    public void publishSellerReportEvent(SellerReportCreateEvent event) {

        CompletableFuture<SendResult<@NonNull String, @NonNull Object>> future = kafkaTemplate.send(SELLER_REPORT_TOPIC, event.getSellerId(), event);

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
    public void publishSendNotificationEvent(SendNotificationEvent event) {

        CompletableFuture<SendResult<@NonNull String, @NonNull Object>> future = kafkaTemplate.send(SEND_NOTIFICATION_TOPIC, event.getTo(), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish seller report event", ex);
            } else {
                log.info("Send notification event published successfully | topic={} | partition={} | offset={}",
                        SEND_NOTIFICATION_TOPIC, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });
    }

    @Override
    public void publishUpdateProductStockEvent(ProductUpdateStockEvent event) {
        CompletableFuture<SendResult<@NonNull String, @NonNull Object>> future = kafkaTemplate.send(ORDER_CONFIRMED_CANCEL_PRODUCT_UPDATE_STOCK_TOPIC, event.getProductId(), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish product update stock event", ex);
            } else {
                log.info("Product update stock event published successfully | topic={} | partition={} | offset={}",
                        ORDER_CONFIRMED_CANCEL_PRODUCT_UPDATE_STOCK_TOPIC, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });
    }

}
