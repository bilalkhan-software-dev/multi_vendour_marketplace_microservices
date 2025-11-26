package com.vendor_marketplace.order_service.dao.repository;

import com.vendor_marketplace.order_service.models.entity.Order;
import com.vendor_marketplace.order_service.models.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {


}
