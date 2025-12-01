package com.vendor_marketplace.review_wishlist_service.mapper;

import com.vendor_marketplace.review_wishlist_service.models.dto.response.WishlistResponse;
import com.vendor_marketplace.review_wishlist_service.models.entity.Wishlist;

import java.util.Collections;

public class WishlistMapper {

    private WishlistMapper() {
    }

    public static WishlistResponse toWishlistResponse(Wishlist wishlist) {
        return WishlistResponse.builder()
                .id(wishlist.getId())
                .userId(wishlist.getUserId())
                .createdAt(wishlist.getCreatedAt())
                .updatedAt(wishlist.getUpdatedAt())
                .products(wishlist.getProductIds().isEmpty() ? Collections.emptySet() : wishlist.getProductIds())
                .build();
    }
}
