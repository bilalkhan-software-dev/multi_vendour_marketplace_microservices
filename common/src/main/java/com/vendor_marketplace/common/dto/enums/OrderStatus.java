package com.vendor_marketplace.common.dto.enums;

public enum OrderStatus {
    PENDING,           // Order created, payment pending
    PAYMENT_FAILED,    // Payment processing failed
    CONFIRMED,         // Payment successful, stock confirmed
    PACKED,           // Items packed in warehouse
    SHIPPED,          // Handover to courier
    OUT_FOR_DELIVERY, // Courier en route
    DELIVERED,        // Successfully delivered
    CANCELLED,        // Cancelled before delivery
    RETURN_REQUESTED,  // Customer requested return
    RETURNED          // Return completed
}
