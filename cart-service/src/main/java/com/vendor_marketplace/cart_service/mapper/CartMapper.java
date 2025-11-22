package com.vendor_marketplace.cart_service.mapper;

import com.vendor_marketplace.cart_service.models.entity.Cart;
import com.vendor_marketplace.cart_service.models.entity.CartItem;
import com.vendor_marketplace.common.dto.response.CartResponse;

import java.util.stream.Collectors;

public class CartMapper {

    public static CartResponse toCartResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .totalMrpPrice(cart.getTotalMrpPrice())
                .totalSellingPrice(cart.getTotalSellingPrice())
                .discount(cart.getDiscount())
                .totalItems(cart.getTotalItems())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .cartItems(cart.getCartItems()
                        .stream()
                        .map(CartMapper::toCartItemResponse
                ).collect(Collectors.toSet()))
                .build();
    }

    public static CartResponse.CartItemResponse toCartItemResponse(CartItem cartItem) {
      return CartResponse.CartItemResponse.builder()
                .id(cartItem.getId())
              .cartId(cartItem.getCartId())
                .userId(cartItem.getUserId())
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .mrpPrice(cartItem.getMrpPrice())
                .sellingPrice(cartItem.getSellingPrice())
                .build();
    }

}
