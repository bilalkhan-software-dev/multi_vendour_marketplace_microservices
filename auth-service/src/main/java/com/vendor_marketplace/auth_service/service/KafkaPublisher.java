package com.vendor_marketplace.auth_service.service;

import com.vendor_marketplace.common.dto.event.SellerCreatedEvent;
import com.vendor_marketplace.common.dto.event.SendOTPEvent;
import com.vendor_marketplace.common.dto.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static com.vendor_marketplace.common.constants.KafkaTopicsConstant.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserCreatedEvent(UserCreatedEvent event) {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(USER_CREATED_TOPIC,event.getAuthId() ,event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("User created event published successfully for user: {}. Offset: {}",
                            event.getEmail(), result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to publish user created event for user: {}", event.getEmail(), ex);
                }
            });
        } catch (Exception e) {
            log.error("Exception while publishing user created event for user: {}", event.getEmail(), e);
        }
    }

    public void publishSellerCreatedEvent(SellerCreatedEvent event) {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(SELLER_CREATED_TOPIC,event.getAuthId(), event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Seller created event published successfully for seller: {}. Offset: {}",
                            event.getEmail(), result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to publish seller created event for seller: {}", event.getEmail(), ex);
                }
            });
        } catch (Exception e) {
            log.error("Exception while publishing seller created event for seller: {}", event.getEmail(), e);
        }
    }

    public void publishSendOTPEvent(SendOTPEvent event) {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(SEND_OTP_TOPIC,event.getTo(), event);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Send OTP event published successfully for auth user: {}. Offset: {}",
                            event.getTo(), result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to publish send OTP event for seller: {}", event.getTo(), ex);
                }
            });
        } catch (Exception e) {
            log.error("Exception while publishing send OTP event for seller: {}", event.getTo(), e);
        }
    }
}