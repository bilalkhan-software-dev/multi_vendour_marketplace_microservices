package com.vendor_marketplace.review_wishlist_service.dao.repository;

import com.vendor_marketplace.review_wishlist_service.models.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


}
