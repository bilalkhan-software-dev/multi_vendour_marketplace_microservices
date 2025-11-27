package com.vendor_marketplace.review_wishlist_service.dao.implementation;

import com.vendor_marketplace.review_wishlist_service.dao.interfaces.WishlistDao;
import com.vendor_marketplace.review_wishlist_service.dao.repository.WishlistRepository;
import com.vendor_marketplace.review_wishlist_service.models.entity.Wishlist;
import com.vendor_marketplace.review_wishlist_service.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class WishlistDaoImpl implements WishlistDao {

    private final WishlistRepository wishlistRepository;

    @Override
    public Wishlist save(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    @Override
    public Optional<Wishlist> findByUser(String userId){
        return wishlistRepository.findByUserId(userId);
    }


    @Override
    public Optional<Wishlist> findById(Long id){
        return wishlistRepository.findById(id);
    }

    /**
     * For admin
     */
    @Override
    public Page<Wishlist> findAll(int page, int size, boolean isNewest) {
        Pageable pageable = PageUtils.toBuildSortAndPage(page, size, isNewest);
        return wishlistRepository.findAll(pageable);
    }









}
