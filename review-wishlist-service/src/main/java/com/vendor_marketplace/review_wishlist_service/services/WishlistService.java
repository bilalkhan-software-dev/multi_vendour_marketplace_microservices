package com.vendor_marketplace.review_wishlist_service.services;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.review_wishlist_service.models.dto.request.AddProductToWishlist;
import com.vendor_marketplace.review_wishlist_service.models.dto.response.WishlistResponse;

public interface WishlistService {
    WishlistResponse addProductToWishlist(String userId, AddProductToWishlist request);

    WishlistResponse getWishlist(String userId);

    WishlistResponse getWishlistById(Long id);

    PagedResponse<WishlistResponse> getAllWishlists(int page, int size, boolean isNewest);
}
