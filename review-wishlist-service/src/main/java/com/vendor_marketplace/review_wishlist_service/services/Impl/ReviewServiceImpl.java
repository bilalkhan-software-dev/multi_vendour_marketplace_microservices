package com.vendor_marketplace.review_wishlist_service.services.Impl;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.common.exception.UnauthorizedException;
import com.vendor_marketplace.review_wishlist_service.dao.interfaces.ReviewDao;
import com.vendor_marketplace.review_wishlist_service.exception.NotUpdateAbleException;
import com.vendor_marketplace.review_wishlist_service.mapper.ReviewMapper;
import com.vendor_marketplace.review_wishlist_service.models.dto.request.AddReviewRequest;
import com.vendor_marketplace.review_wishlist_service.models.dto.request.UpdateReviewRequest;
import com.vendor_marketplace.review_wishlist_service.models.dto.response.ReviewResponse;
import com.vendor_marketplace.review_wishlist_service.models.entity.Review;
import com.vendor_marketplace.review_wishlist_service.services.ReviewService;

import com.vendor_marketplace.review_wishlist_service.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
class ReviewServiceImpl implements ReviewService {


    private final ReviewDao reviewDao;
    private final ProductService productService;

    @Override
    @Transactional
    public ReviewResponse addReview(AddReviewRequest request, String userId) {

        String productId = request.getProductId();

        if (!productService.isProductExist(productId)) {
            throw new ResourceNotFoundException("Product not found with Id: " + productId);
        }

        Review review = Review.builder()
                .comment(request.getComment())
                .rating(request.getRating())
                .productId(productId)
                .userId(userId)
                .images(request.getImages())
                .build();

        Review saved = reviewDao.save(review);

        return ReviewMapper.toReviewResponse(saved);
    }


    @Override
    @Transactional
    public ReviewResponse updateReview(Long id, String userId, UpdateReviewRequest request) {

        Review existingReview = reviewDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Review not found with id: " + id)
        );

        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);
        boolean isTwoDaysAgo = existingReview.getCreatedAt().isBefore(twoDaysAgo);
        if (isTwoDaysAgo) {
            throw new NotUpdateAbleException("You cannot update review after 48 hours of creation.");
        }

        if (!existingReview.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can't update another user review");
        }

        if (request.getComment() != null) {
            existingReview.setComment(request.getComment());
        }

        if (request.getRating() != null) {
            existingReview.setRating(request.getRating());
        }

        if (request.getImages() != null) {
            existingReview.setImages(request.getImages());
        }

        Review updated = reviewDao.save(existingReview);
        return ReviewMapper.toReviewResponse(updated);
    }


    @Override
    public ReviewResponse getById(Long id) {
        return ReviewMapper.toReviewResponse(reviewDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Review not found with id: " + id)
        ));
    }

    @Override
    @Transactional
    public void deleteReview(String userId, Long id) {

        Review existingReview = reviewDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Review not found with id: " + id)
        );

        if (!existingReview.getUserId().equals(userId)) {
            throw new UnauthorizedException("You can't delete another user review");
        }

        reviewDao.deleteById(id);

    }

    @Override
    public PagedResponse<ReviewResponse> getUserReviews(String userId, int page, int size, boolean isNewest) {

        Page<Review> reviews = reviewDao.findByUser(userId, page, size, isNewest);

        return PageUtils.buildPagedResponse(reviews, ReviewMapper::toReviewResponse);
    }

    @Override
    public PagedResponse<ReviewResponse> getProductReviews(String productId, int page, int size, boolean isNewest) {

        Page<Review> reviews = reviewDao.findByProduct(productId, page, size, isNewest);

        return PageUtils.buildPagedResponse(reviews, ReviewMapper::toReviewResponse);
    }

    @Override
    public PagedResponse<ReviewResponse> getAllReviews(int page, int size, boolean isNewest) {

        Page<Review> reviews = reviewDao.findAll(page, size, isNewest);

        return PageUtils.buildPagedResponse(reviews, ReviewMapper::toReviewResponse);
    }


}
