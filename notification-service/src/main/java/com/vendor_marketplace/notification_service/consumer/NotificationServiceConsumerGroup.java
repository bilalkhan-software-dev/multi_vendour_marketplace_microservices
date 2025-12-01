package com.vendor_marketplace.notification_service.consumer;

import com.vendor_marketplace.common.dto.event.SendNotificationEvent;
import com.vendor_marketplace.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.mail.MailSendException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;


import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.SEND_NOTIFICATION_TOPIC;


@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceConsumerGroup {

    private final EmailService emailService;

    // Consumer 1
    @RetryableTopic(
            attempts = "2",
            backoff = @Backoff(delay = 5000, multiplier = 2, maxDelay = 30000),
            numPartitions = "3"
    )
    @KafkaListener(
            topics = SEND_NOTIFICATION_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory" //optional
    )
    public void consumer1(
            @Payload final SendNotificationEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment acknowledgment) {
        processEvent(event, key, partition, offset, acknowledgment, "Consumer-1");
    }

    // Consumer 2
    @RetryableTopic(
            attempts = "2",
            backoff = @Backoff(delay = 4000, multiplier = 1.5, maxDelay = 15000),
            numPartitions = "3"
    )
    @KafkaListener(
            topics = SEND_NOTIFICATION_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumer2(
            @Payload final SendNotificationEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment acknowledgment) {
        processEvent(event, key, partition, offset, acknowledgment, "Consumer-2");
    }

    // Consumer 3
    @RetryableTopic(
            attempts = "2",
            backoff = @Backoff(delay = 4000, multiplier = 1.5, maxDelay = 15000),
            numPartitions = "3"
    )
    @KafkaListener(
            topics = SEND_NOTIFICATION_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumer3(
            @Payload final SendNotificationEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment acknowledgment) {
        processEvent(event, key, partition, offset, acknowledgment, "Consumer-3");
    }

    private void processEvent(SendNotificationEvent event, String key, Integer partition,
                              Long offset, Acknowledgment acknowledgment, String consumerId) {
        try {
            log.info("{} - Received: {} - Key: {}, Partition: {}, Offset: {}, Email: {}",
                    consumerId, event.getEventType(), key, partition, offset, event.getTo());

            emailService.sendEmail(event);
            acknowledgment.acknowledge();
            log.info("{} - Successfully processed for user: {}", consumerId, event.getTo());

        } catch (MailSendException e) {
            log.warn("{} - Mail send error for user: {}", consumerId, event.getTo());
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("{} - Error processing for user: {}", consumerId, event.getTo(), e);
        }
    }

    @DltHandler
    public void listenDLT(SendNotificationEvent event,
                          @Header(KafkaHeaders.RECEIVED_KEY) String key,
                          @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                          @Header(KafkaHeaders.OFFSET) Long offset,
                          Acknowledgment ack
                          ) {
        log.info("Received DLT - Key: {}, Partition: {}, Offset: {}, Email: {}",
                key, partition, offset, event.getTo());

//            emailService.sendEmail(event);
        log.info("Dlt Event: {}", event);
        ack.acknowledge();


    }
}

