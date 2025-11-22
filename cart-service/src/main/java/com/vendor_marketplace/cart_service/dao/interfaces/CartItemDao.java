package com.vendor_marketplace.cart_service.dao.interfaces;

import com.vendor_marketplace.cart_service.models.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemDao {
    CartItem findByProductIdAndUserId(String productId, String userId);

    CartItem saveCartItem(CartItem cartItem);

    Optional<CartItem> findById(Long id);

    List<CartItem> findAllItemByCartOrUserId(String cartId, String userId);

    void deleteItemFromCart(CartItem cartItem);
}
