package com.vendor_marketplace.review_wishlist_service.dao.interfaces;

import com.vendor_marketplace.review_wishlist_service.models.entity.Review;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ReviewDao {
    Review save(Review review);

    boolean existById(Long id);

    void deleteById(Long id);

    Optional<Review> findById(Long id);

    Page<Review> findByProduct(String productId, int page, int size, boolean isNewest);

    Page<Review> findByUser(String userId, int page, int size, boolean isNewest);

    Page<Review> findAll(int page, int size, boolean isNewest);
}
