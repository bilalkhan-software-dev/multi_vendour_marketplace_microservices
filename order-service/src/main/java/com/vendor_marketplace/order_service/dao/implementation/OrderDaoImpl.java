package com.vendor_marketplace.order_service.dao.implementation;

import com.vendor_marketplace.order_service.dao.interfaces.OrderDao;
import com.vendor_marketplace.order_service.dao.repository.OrderRepository;
import com.vendor_marketplace.order_service.models.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class OrderDaoImpl implements OrderDao {

    private final OrderRepository orderRepository;

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    // for admin  :> in future adding multiple sort filter for admin
    @Override
    public Page<Order> findAll(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }


    public List<Order> findByOrderId(String orderId){

    }









}
