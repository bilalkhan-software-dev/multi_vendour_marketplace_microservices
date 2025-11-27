package com.vendor_marketplace.review_wishlist_service.mapper;

import com.vendor_marketplace.review_wishlist_service.models.dto.response.ReviewResponse;
import com.vendor_marketplace.review_wishlist_service.models.entity.Review;

import java.util.Collections;

public class ReviewMapper {

    private ReviewMapper() {}

    public static ReviewResponse toReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .productId(review.getProductId())
                .comment(review.getComment())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .images(review.getImages().isEmpty() ? Collections.emptyList() : review.getImages())
                .build();
    }

}
