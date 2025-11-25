package com.vendor_marketplace.order_service.services.Impl;

import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.order_service.dao.interfaces.OrderItemDao;
import com.vendor_marketplace.order_service.mapper.OrderItemMapper;
import com.vendor_marketplace.order_service.models.dto.response.OrderItemResponse;
import com.vendor_marketplace.order_service.services.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemDao orderItemDao;

    @Override
    public OrderItemResponse findById(Long id) {
        return OrderItemMapper.toOrderItemResponse(orderItemDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order item not found with id: " + id)
        ));
    }




}
