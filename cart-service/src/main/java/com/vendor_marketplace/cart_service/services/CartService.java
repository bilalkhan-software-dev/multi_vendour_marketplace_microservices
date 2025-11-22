package com.vendor_marketplace.cart_service.services;

import com.vendor_marketplace.cart_service.models.dto.request.AddToCartItemRequest;
import com.vendor_marketplace.common.dto.response.CartResponse;


public interface CartService {

    CartResponse getUserCart(String userId);

    CartResponse.CartItemResponse addItemToCart(String userId, AddToCartItemRequest request);



}
