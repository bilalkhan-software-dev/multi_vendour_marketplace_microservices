package com.vendor_marketplace.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/authService")
    public Mono<ResponseEntity<Map<String, Object>>> authServiceFallback() {
        return createFallbackResponse("Authentication service is temporarily unavailable", "AUTH_SERVICE_DOWN");
    }

    @GetMapping("/userService")
    public Mono<ResponseEntity<Map<String, Object>>> userServiceFallback() {
        return createFallbackResponse("User service is currently unavailable", "USER_SERVICE_DOWN");
    }

    @GetMapping("/sellerService")
    public Mono<ResponseEntity<Map<String, Object>>> sellerServiceFallback() {
        return createFallbackResponse("Seller service is currently unavailable", "SELLER_SERVICE_DOWN");
    }

    @GetMapping("/homeService")
    public Mono<ResponseEntity<Map<String, Object>>> homeServiceFallBack() {
        return createFallbackResponse("Home Service service is currently unavailable", "HOME_SERVICE_DOWN");
    }

    @GetMapping("/reviewService")
    public Mono<ResponseEntity<Map<String, Object>>> reviewServiceFallBack() {
        return createFallbackResponse("Review Service service is currently unavailable", "REVIEW_SERVICE_DOWN");
    }

    @GetMapping("/paymentService")
    public Mono<ResponseEntity<Map<String, Object>>> paymentServiceFallBack() {
        return createFallbackResponse("Payment Service service is currently unavailable", "PAYMENT_SERVICE_DOWN");
    }

    @GetMapping("/orderService")
    public Mono<ResponseEntity<Map<String, Object>>> orderServiceFallBack() {
        return createFallbackResponse("Order Service service is currently unavailable", "ORDER_SERVICE_DOWN");
    }

    @GetMapping("/transactionService")
    public Mono<ResponseEntity<Map<String, Object>>> transactionServiceFallBack() {
        return createFallbackResponse("Transaction-Report Service service is currently unavailable", "TRANSACTION_REPORT_SERVICE_DOWN");
    }

    @GetMapping("/cartService")
    public Mono<ResponseEntity<Map<String, Object>>> cartServiceFallBack() {
        return createFallbackResponse("Cart Service service is currently unavailable", "CART_SERVICE_DOWN");
    }

    @GetMapping("/productQueryService")
    public Mono<ResponseEntity<Map<String, Object>>> productQueryServiceFallBack() {
        return createFallbackResponse("Product Query Service service is currently unavailable", "PRODUCT_QUERY_SERVICE_DOWN");
    }
    @GetMapping("/productCommandService")
    public Mono<ResponseEntity<Map<String, Object>>> productCommandServiceFallBack() {
        return createFallbackResponse("Product Command Service service is currently unavailable", "PRODUCT_COMMAND_SERVICE_DOWN");
    }


    private Mono<ResponseEntity<Map<String, Object>>> createFallbackResponse(String message, String errorCode) {
        return Mono.fromSupplier(() -> {
            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now());
            response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
            response.put("error", "Service Unavailable");
            response.put("message", message);
            response.put("errorCode", errorCode);

            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(response);
        });
    }
}