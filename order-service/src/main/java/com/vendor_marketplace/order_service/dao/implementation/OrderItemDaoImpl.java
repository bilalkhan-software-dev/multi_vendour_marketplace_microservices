package com.vendor_marketplace.order_service.dao.implementation;

import com.vendor_marketplace.order_service.dao.interfaces.OrderItemDao;
import com.vendor_marketplace.order_service.dao.repository.OrderItemRepository;
import com.vendor_marketplace.order_service.models.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class OrderItemDaoImpl implements OrderItemDao {

    private final OrderItemRepository orderItemRepository;

    @Override
    public Optional<OrderItem> findById(Long id) {
        return orderItemRepository.findById(id);
    }







}
