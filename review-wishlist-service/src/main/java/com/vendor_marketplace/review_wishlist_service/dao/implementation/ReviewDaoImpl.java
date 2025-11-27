package com.vendor_marketplace.review_wishlist_service.dao.implementation;

import com.vendor_marketplace.review_wishlist_service.dao.interfaces.ReviewDao;
import com.vendor_marketplace.review_wishlist_service.dao.repository.ReviewRepository;
import com.vendor_marketplace.review_wishlist_service.models.entity.Review;
import com.vendor_marketplace.review_wishlist_service.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class ReviewDaoImpl implements ReviewDao {

    private final ReviewRepository reviewRepository;


    @Override
    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public boolean existById(Long id) {
        return reviewRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public Page<Review> findByUser(String userId, int page, int size, boolean isNewest) {

        Pageable pageable = PageUtils.toBuildSortAndPage(page, size, isNewest);
        return reviewRepository.findByUserId(userId, pageable);

    }

    @Override
    public Page<Review> findByProduct(String productId, int page, int size, boolean isNewest) {
        Pageable pageable = PageUtils.toBuildSortAndPage(page, size, isNewest);
        return reviewRepository.findByProductId(productId, pageable);

    }

    @Override
    public Page<Review> findAll(int page, int size, boolean isNewest) {

        Pageable pageable = PageUtils.toBuildSortAndPage(page, size, isNewest);
        return reviewRepository.findAll(pageable);

    }


}
