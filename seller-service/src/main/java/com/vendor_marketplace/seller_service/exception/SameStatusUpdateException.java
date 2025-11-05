package com.vendor_marketplace.seller_service.exception;

public class SameStatusUpdateException extends RuntimeException {
    public SameStatusUpdateException(String message) {
        super(message);
    }
}
