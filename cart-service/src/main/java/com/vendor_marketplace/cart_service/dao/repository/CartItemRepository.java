package com.vendor_marketplace.cart_service.dao.repository;

import com.vendor_marketplace.cart_service.models.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CartItemRepository extends JpaRepository<CartItem,Long> {


    CartItem findByProductIdAndUserId(String productId, String userId);

    List<CartItem> findByCartIdOrUserId(String cartId, String userId);

    List<CartItem> findByCartId(String cartId);

    List<CartItem> findByUserId(String userId);

}
