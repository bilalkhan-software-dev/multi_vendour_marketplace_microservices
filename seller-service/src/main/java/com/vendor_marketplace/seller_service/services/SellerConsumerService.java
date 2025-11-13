package com.vendor_marketplace.seller_service.services;

import com.vendor_marketplace.common.dto.event.SellerCreatedEvent;
import com.vendor_marketplace.seller_service.exception.ExistDataException;
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

import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.SELLER_CREATED_TOPIC;


@Service
@Slf4j
@RequiredArgsConstructor
public class SellerConsumerService {

    private final SellerService sellerService;

    @RetryableTopic(
            attempts = "2",
            exclude = {ExistDataException.class},
            backoff = @Backoff(delay = 4000, multiplier = 1.5, maxDelay = 15000),
            numPartitions = "3"
    )
    @KafkaListener(topics = SELLER_CREATED_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSellerCreatedEvent(
            @Payload final SellerCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment ack
    ) {
        try {
            log.info("Received SellerCreatedEvent - Key: {}, Partition: {}, Offset: {}, Topic: {} Email: {}",
                    key, partition, offset, topic, event.getEmail());

            sellerService.registerSeller(event);

            ack.acknowledge();
            log.info("Successfully processed SellerCreatedEvent for user: {}", event.getEmail());

        } catch (ExistDataException e) {
            log.warn("Seller already exists: {}", event.getEmail());
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing SellerCreatedEvent for user: {}", event.getEmail(), e);
            e.printStackTrace();
        }
    }
    @DltHandler
    public void listenDLT(SellerCreatedEvent event,
                          @Header(KafkaHeaders.RECEIVED_KEY) String key,
                          @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                          @Header(KafkaHeaders.OFFSET) Long offset
    ) {
        log.info("Received SellerCreatedEvent DLT - Key: {}, Partition: {}, Offset: {}, Email: {}",key,partition,offset,event.getEmail());
        sellerService.registerSeller(event);
    }
}
