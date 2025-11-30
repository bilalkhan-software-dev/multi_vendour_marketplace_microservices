package com.vendor_marketplace.user_service.service;

import com.vendor_marketplace.common.dto.event.UserCreatedEvent;
import com.vendor_marketplace.common.exception.ExistDataException;
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

import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.USER_CREATED_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserConsumerService {

    private final UserService userService;

    @RetryableTopic(
            attempts = "2",
            exclude = {ExistDataException.class},
            backoff = @Backoff(
                    delay = 5000,            // Start with 5 seconds
                    multiplier = 2,          // Double each time: 5s, 10s, 20s
                    maxDelay = 30000         // Cap at 30 seconds
            ),
            numPartitions = "3"
    )
    @KafkaListener(topics = USER_CREATED_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUserCreatedEvent(
            @Payload final UserCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment acknowledgment) {

        try {
            log.info("Received UserCreatedEvent - Key: {}, Partition: {}, Offset: {}, Email: {}",
                    key, partition, offset, event.getEmail());


            // Process the event
            userService.registerUser(event);

            // Manually acknowledge the message committing if successful
            acknowledgment.acknowledge();
            log.info("Successfully processed UserCreatedEvent for user: {}", event.getEmail());

        } catch (ExistDataException e) {
            log.warn("User already exists: {}", event.getEmail());
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error processing UserCreatedEvent for user: {}", event.getEmail(), e);
            throw new RuntimeException("Processing failed for user: " + event.getEmail(), e);
        }
    }

    @DltHandler
    public void listenDLT(UserCreatedEvent event,
                          @Header(KafkaHeaders.RECEIVED_KEY) String key,
                          @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                          @Header(KafkaHeaders.OFFSET) Long offset,
                          Acknowledgment acknowledgment
    ) {
        log.info("Received UserCreatedEvent DLT - Key: {}, Partition: {}, Offset: {}, Email: {}", key, partition, offset, event.getEmail());
        log.info("Event: {}", event);
        acknowledgment.acknowledge();
    }
}