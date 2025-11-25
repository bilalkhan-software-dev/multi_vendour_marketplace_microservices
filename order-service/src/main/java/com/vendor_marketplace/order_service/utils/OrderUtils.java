package com.vendor_marketplace.order_service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendor_marketplace.common.dto.response.CartResponse;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.order_service.feignClient.CartServiceClient;
import com.vendor_marketplace.order_service.feignClient.UserServiceClient;
import com.vendor_marketplace.order_service.models.dto.response.UserAddressResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderUtils {

    private final UserServiceClient userService;
    private final CartServiceClient cartService;
    private final ObjectMapper objectMapper;


    public UserAddressResponse fetchUserShippedAddress(String userId, Long id) {
        ResponseEntity<UserAddressResponse> response = userService.getAddressById(id);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("Error while getting shipping address: {} for user(customer): {}", id, userId);
            throw new ResourceNotFoundException("Address not found");
        }
        return response.getBody();
    }

    public CartResponse fetchUserCart(String userId) {

        ResponseEntity<Map<String, Object>> response = cartService.getUserCart(userId);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Cart not found | userId={} | status={}", userId, response.getStatusCode());
            throw new ResourceNotFoundException("Cart not found");
        }

        Map<String, Object> body = response.getBody();
        if (body == null || body.get("data") == null) {
            log.error("Cart lookup returned empty body | userId={}", userId);
            throw new ResourceNotFoundException("Cart not found");
        }

        CartResponse cart = objectMapper.convertValue(body.get("data"), CartResponse.class);
        log.info("Cart found | userId={}", userId);
        return cart;
    }

    public String generateOrderId() {
        LocalDateTime now = LocalDateTime.now(); // 2025-08-30T23:15:45
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")); //20250830231545
        int randomSuffix = (int) (Math.random() * 1000); // 342

        return "ORD-" + timestamp + "-" + String.format("%03d", randomSuffix); // ORD-20250830231545-342
    }
}