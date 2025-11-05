package com.vendor_marketplace.user_service.exception;

public class SameStatusUpdateException extends RuntimeException {
    public SameStatusUpdateException(String message) {
        super(message);
    }
}
