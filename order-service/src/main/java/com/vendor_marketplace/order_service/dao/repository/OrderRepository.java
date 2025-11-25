package com.vendor_marketplace.order_service.dao.repository;

import com.vendor_marketplace.order_service.models.entity.Order;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByOrderId(String orderId);

    Page<Order> findBySellerId(String sellerId, Pageable pageable);

    Page<Order> findByUserId(String userId, Pageable pageable);

}
