package com.vendor_marketplace.gateway.handler.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.vendor_marketplace.gateway.handler.security.JsonServerAuthenticationEntryPoint.getResponse;

@Component
public class JsonServerAccessDeniedHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return writeJsonError(exchange, HttpStatus.FORBIDDEN, "FORBIDDEN", denied.getMessage());
    }

    private Mono<Void> writeJsonError(ServerWebExchange exchange, HttpStatus status, String code, String message) {
        return getResponse(exchange, status, code, message);
    }
}

