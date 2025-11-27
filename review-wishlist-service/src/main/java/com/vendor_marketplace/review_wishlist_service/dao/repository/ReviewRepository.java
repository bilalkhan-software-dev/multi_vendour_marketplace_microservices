package com.vendor_marketplace.review_wishlist_service.dao.repository;

import com.vendor_marketplace.review_wishlist_service.models.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    Page<Review> findByUserId(String userId, Pageable pageable);

    Page<Review> findByProductId(String productId, Pageable pageable);
}
