package com.vendor_marketplace.cart_service.dao.interfaces;

import com.vendor_marketplace.cart_service.models.entity.Cart;

import java.util.Optional;

public interface CartDao {

    Cart save(Cart cart);

    Optional<Cart> findCartByUserId(String userId);

    Optional<Cart> findCartById(String cartId);
}
