package com.vendor_marketplace.order_service.dao.interfaces;

import com.vendor_marketplace.order_service.models.entity.Order;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface OrderDao {
    void save(Order order);

    // for admin
    Page<Order> findAll(int page, int size);

    Optional<Order> findById(Long id);
}
