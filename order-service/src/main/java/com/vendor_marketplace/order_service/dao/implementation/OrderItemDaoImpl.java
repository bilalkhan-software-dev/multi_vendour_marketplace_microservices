package com.vendor_marketplace.order_service.dao.implementation;

import com.vendor_marketplace.order_service.dao.interfaces.OrderItemDao;
import com.vendor_marketplace.order_service.dao.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class OrderItemDaoImpl implements OrderItemDao {

    private final OrderItemRepository orderItemRepository;



}
