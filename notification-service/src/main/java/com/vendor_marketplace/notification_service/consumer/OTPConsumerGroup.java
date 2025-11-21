package com.vendor_marketplace.notification_service.consumer;

import com.vendor_marketplace.common.dto.event.SendOTPEvent;
import com.vendor_marketplace.notification_service.service.EmailService;
import jakarta.mail.MessagingException;
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

import java.io.UnsupportedEncodingException;

import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.SEND_OTP_TOPIC;


@Service
@Slf4j
@RequiredArgsConstructor
public class OTPConsumerGroup {

    private final EmailService emailService;

    @RetryableTopic(
            attempts = "2",
            backoff = @Backoff(delay = 4000, multiplier = 1.5, maxDelay = 15000),
            numPartitions = "3"

    )
    @KafkaListener(
            topics = SEND_OTP_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeOTPEvent(
            @Payload final SendOTPEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment acknowledgment) {

        try {
            log.info("Received SendOTPEvent - Key: {}, Partition: {}, Offset: {}, Email: {}",
                    key, partition, offset, event.getTo());

            // Process the event
            emailService.sendEmail(event);
            // Manually acknowledge the message committing if successful
            acknowledgment.acknowledge();
            log.info("Successfully processed SendOTPEvent for user: {}", event.getTo());

        } catch (MailSendException e) {
            log.warn("Error when sending mail for user: {}", event.getTo());
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error processing SendOTPEvent for user: {}", event.getTo(), e);
        }
    }

    @DltHandler
    public void listenDLT(SendOTPEvent event,
                          @Header(KafkaHeaders.RECEIVED_KEY) String key,
                          @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                          @Header(KafkaHeaders.OFFSET) Long offset
    ) {
        log.info("Received SendOTPEvent DLT - Key: {}, Partition: {}, Offset: {}, Email: {}", key, partition, offset, event.getTo());

        try {
            emailService.sendEmail(event);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
