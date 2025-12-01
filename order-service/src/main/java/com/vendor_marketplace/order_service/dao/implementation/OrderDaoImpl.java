package com.vendor_marketplace.order_service.dao.implementation;

import com.vendor_marketplace.common.dto.enums.OrderStatus;
import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.order_service.dao.interfaces.OrderDao;
import com.vendor_marketplace.order_service.dao.repository.OrderRepository;
import com.vendor_marketplace.order_service.models.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class OrderDaoImpl implements OrderDao {

    private final OrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> saveAll(List<Order> orders) {
       return orderRepository.saveAll(orders);
    }

    // for admin  :> in future adding multiple sort filter for admin
    @Override
    public Page<Order> findAll(int page, int size, boolean isNewest) {
        Sort sort = Sort.by(isNewest ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }


    @Override
    public List<Order> findByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    @Override
    public Page<Order> findBySeller(String sellerId, int page, int size, boolean isNewest) {
        Sort sort = Sort.by(isNewest ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findBySellerId(sellerId, pageable);
    }

    @Override
    public Page<Order> findByUser(String userId, int page, int size, boolean isNewest) {
        Sort sort = Sort.by(isNewest ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findByUserId(userId, pageable);
    }

    @Override
    public void deleteById(Long id) {
        log.info("🗑Deleting orders | order Id={}", id);
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }

        orderRepository.deleteById(id);
        log.info("Deleted successfully : order id: {}", id);
    }

    @Override
    public void deleteOrderIdOrders(String orderId) {
        log.info("🗑 Deleting orders | orderId={}", orderId);

        List<Order> orders = orderRepository.findByOrderId(orderId);

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("Order not found with id: " + orderId);
        }

        orderRepository.deleteAll(orders);

        log.info("Delete completed | orderId={} | deletedCount={}", orderId, orders.size());
    }

    @Override
    public int updateOrderAndPaymentStatus(String orderId, OrderStatus orderStatus, PaymentStatus paymentStatus
    ) {
        return orderRepository.bulkUpdateOrders(orderId, orderStatus, paymentStatus, LocalDateTime.now());
    }

    @Override
    public boolean existByOrderId(String orderId) {
        return orderRepository.existsByOrderId(orderId);
    }

    @Override
    public boolean existByOrderIdAndCreatedAtBefore(String orderId, LocalDateTime cutoffTime) {
        return orderRepository.existsByOrderIdAndCreatedAtBefore(orderId, cutoffTime);
    }

    @Override
    public List<Order> findByOrderIdAndUserId(String orderId, String userId) {
        return orderRepository.findByOrderIdAndUserId(orderId, userId);
    }
}
