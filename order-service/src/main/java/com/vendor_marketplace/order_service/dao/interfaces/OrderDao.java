package com.vendor_marketplace.order_service.dao.interfaces;

import com.vendor_marketplace.common.dto.enums.OrderStatus;
import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import com.vendor_marketplace.order_service.models.entity.Order;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Order save(Order order);

    List<Order> saveAll(List<Order> orders);

    // for admin
    Page<Order> findAll(int page, int size, boolean isNewest);

    Optional<Order> findById(Long id);

    // for admin
    List<Order> findByOrderId(String orderId);

    Page<Order> findBySeller(String sellerId, int page, int size, boolean isNewest);

    Page<Order> findByUser(String userId, int page, int size, boolean isNewest);

    void deleteById(Long id);

    // for admin
    void deleteOrderIdOrders(String orderId);

    // for admin/kafka event consumer
    int updateOrderAndPaymentStatus(String orderId, OrderStatus orderStatus, PaymentStatus paymentStatus
    );

    boolean existByOrderId(String orderId);

    boolean existByOrderIdAndCreatedAtBefore(String orderId, LocalDateTime cutoffTime);

    List<Order> findByOrderIdAndUserId(String orderId, String userId);
}
