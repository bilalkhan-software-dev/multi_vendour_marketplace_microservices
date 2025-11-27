package com.vendor_marketplace.review_wishlist_service.dao.interfaces;

import com.vendor_marketplace.review_wishlist_service.models.entity.Wishlist;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface WishlistDao {
    Wishlist save(Wishlist wishlist);

    Optional<Wishlist> findByUser(String userId);

    Optional<Wishlist> findById(Long id);

    Page<Wishlist> findAll(int page, int size, boolean isNewest);

}
