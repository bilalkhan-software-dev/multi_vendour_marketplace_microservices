package com.vendor_marketplace.cart_service.services;

import com.vendor_marketplace.cart_service.models.dto.request.UpdateCartItemRequest;
import com.vendor_marketplace.common.dto.response.CartResponse;

import java.util.List;

public interface CartItemService {

    CartResponse.CartItemResponse updateCartItem(String userId, Long cartItemId, UpdateCartItemRequest cartItemRequest);

    void removeCartItem(String userId, Long cartItemId);

    CartResponse.CartItemResponse findCartItemById(Long cartItemId);

    List<CartResponse.CartItemResponse> getCartItemsByUserOrCartId(String userId, String cartId);

    void clearUserCart(String userId);
}
