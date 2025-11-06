package com.vendor_marketplace.auth_service.exception;

public class KeycloakOperationException extends RuntimeException {
    public KeycloakOperationException(String message) {
        super(message);
    }
}
