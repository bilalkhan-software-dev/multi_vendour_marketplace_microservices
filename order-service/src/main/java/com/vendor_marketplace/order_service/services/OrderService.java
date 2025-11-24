package com.vendor_marketplace.order_service.services;

import com.vendor_marketplace.order_service.models.dto.request.CheckoutRequest;

public interface OrderService {
    void placeOrder(String userId, CheckoutRequest request);
}
