package com.vendor_marketplace.order_service.services;

import com.vendor_marketplace.order_service.models.dto.response.OrderItemResponse;

public interface OrderItemService {
    OrderItemResponse findById(Long id);
}
