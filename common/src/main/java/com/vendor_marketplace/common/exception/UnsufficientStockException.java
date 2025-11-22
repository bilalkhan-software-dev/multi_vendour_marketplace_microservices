package com.vendor_marketplace.common.exception;

public class UnsufficientStockException extends RuntimeException {
    public UnsufficientStockException(String message) {
        super(message);
    }
}
