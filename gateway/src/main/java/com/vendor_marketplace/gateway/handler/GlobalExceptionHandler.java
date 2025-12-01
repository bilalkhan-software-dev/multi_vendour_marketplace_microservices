package com.vendor_marketplace.gateway.handler;

import com.vendor_marketplace.gateway.exception.ResourceNotFoundException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<Object>> handleResourceNotFound(
            ResourceNotFoundException ex,
            ServerWebExchange exchange
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", exchange.getRequest().getPath().value());

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(body));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Object>> handleIllegalArgument(
            IllegalArgumentException ex,
            ServerWebExchange exchange
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        body.put("path", exchange.getRequest().getPath().value());

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body));
    }


    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<Object>> handleServiceNotFoundArgument(
            IllegalArgumentException ex,
            ServerWebExchange exchange
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        body.put("error", "Service unavailable");
        body.put("message", ex.getMessage());
        body.put("path", exchange.getRequest().getPath().value());

        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body));
    }
}