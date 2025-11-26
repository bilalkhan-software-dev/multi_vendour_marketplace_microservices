package com.vendor_marketplace.product_command_service.kafka.publisher;

import com.vendor_marketplace.common.dto.event.ProductCreateEvent;
import com.vendor_marketplace.common.dto.event.ProductDeleteEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateStockEvent;
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
public class KafkaPublisherService {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void kafkaProductCreateEventPublisher(ProductCreateEvent event) {
        publishEvent(PRODUCT_CREATE_TOPIC, event.getProductId(), event, "ProductCreateEvent");
    }

    public void kafkaProductUpdateEventPublisher(ProductUpdateEvent event) {
        publishEvent(PRODUCT_UPDATE_TOPIC, event.getProductId(), event, "ProductUpdateEvent");
    }

    public void kafkaProductDeleteEventPublisher(ProductDeleteEvent event) {
        publishEvent(PRODUCT_DELETE_TOPIC, event.getProductId(), event, "ProductDeleteEvent");
    }

    public void kafkaProductUpdateStockEventPublisher(ProductUpdateStockEvent event) {
        publishEvent(PRODUCT_UPDATE_STOCK_TOPIC, event.getProductId(), event, "ProductUpdateStockEvent");
    }

    private void publishEvent(String topic, String key, Object event, String eventType) {
        try {
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(topic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info(
                            "{} published successfully for productId: {} | offset: {} | topic: {}",
                            eventType, key, result.getRecordMetadata().offset(), topic
                    );
                } else {
                    log.error(
                            "Failed to publish {} for productId: {} | topic: {}",
                            eventType, key, topic, ex
                    );
                }
            });
        } catch (Exception e) {
            log.error(
                    "Exception while publishing {} for productId: {} | topic: {}",
                    eventType, key, topic, e
            );
        }
    }
}
