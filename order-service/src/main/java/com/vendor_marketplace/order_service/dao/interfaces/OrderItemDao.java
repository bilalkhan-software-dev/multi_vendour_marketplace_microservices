package com.vendor_marketplace.order_service.dao.interfaces;

import com.vendor_marketplace.order_service.models.entity.OrderItem;

import java.util.Optional;

public interface OrderItemDao {
    Optional<OrderItem> findById(Long id);
}
