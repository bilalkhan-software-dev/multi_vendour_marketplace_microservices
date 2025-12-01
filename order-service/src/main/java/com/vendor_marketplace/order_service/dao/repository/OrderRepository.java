package com.vendor_marketplace.order_service.dao.repository;

import com.vendor_marketplace.common.dto.enums.OrderStatus;
import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import com.vendor_marketplace.order_service.models.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderId(String orderId);

    Page<Order> findBySellerId(String sellerId, Pageable pageable);

    Page<Order> findByUserId(String userId, Pageable pageable);

    @Modifying
    @Query("UPDATE Order o SET o.orderStatus = :orderStatus, o.paymentStatus = :paymentStatus, o.updatedAt = :now WHERE o.orderId = :orderId")
    int bulkUpdateOrders(
            @Param("orderId") String orderId,
            @Param("orderStatus") OrderStatus orderStatus,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            @Param("now") LocalDateTime now
    );

    boolean existsByOrderIdAndCreatedAtBefore(String orderId, LocalDateTime createdAt);

    boolean existsByOrderId(String orderId);


    List<Order> findByOrderIdAndUserId(String orderId, String userId);
}
