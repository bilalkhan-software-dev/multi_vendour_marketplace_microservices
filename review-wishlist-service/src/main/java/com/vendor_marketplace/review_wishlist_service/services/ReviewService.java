package com.vendor_marketplace.review_wishlist_service.services;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.review_wishlist_service.models.dto.request.AddReviewRequest;
import com.vendor_marketplace.review_wishlist_service.models.dto.request.UpdateReviewRequest;
import com.vendor_marketplace.review_wishlist_service.models.dto.response.ReviewResponse;

public interface ReviewService {
    ReviewResponse addReview(AddReviewRequest request, String userId);

    ReviewResponse updateReview(Long id, String userId, UpdateReviewRequest request);

    ReviewResponse getById(Long id);

    PagedResponse<ReviewResponse> getUserReviews(String userId, int page, int size, boolean isNewest);

    PagedResponse<ReviewResponse> getProductReviews(String productId, int page, int size, boolean isNewest);

    PagedResponse<ReviewResponse> getAllReviews(int page, int size, boolean isNewest);

    void deleteReview(String userId, Long id);
}
