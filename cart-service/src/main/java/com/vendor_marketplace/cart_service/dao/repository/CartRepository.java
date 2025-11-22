package com.vendor_marketplace.cart_service.dao.repository;

import com.vendor_marketplace.cart_service.models.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {

    Optional<Cart> findByUserId(String userId);
}
