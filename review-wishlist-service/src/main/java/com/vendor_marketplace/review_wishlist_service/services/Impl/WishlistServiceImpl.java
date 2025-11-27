package com.vendor_marketplace.review_wishlist_service.services.Impl;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.review_wishlist_service.dao.interfaces.WishlistDao;
import com.vendor_marketplace.review_wishlist_service.mapper.WishlistMapper;
import com.vendor_marketplace.review_wishlist_service.models.dto.request.AddProductToWishlist;
import com.vendor_marketplace.review_wishlist_service.models.dto.response.WishlistResponse;
import com.vendor_marketplace.review_wishlist_service.models.entity.Wishlist;
import com.vendor_marketplace.review_wishlist_service.services.WishlistService;
import com.vendor_marketplace.review_wishlist_service.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class WishlistServiceImpl implements WishlistService {

    private final ProductService productService;
    private final WishlistDao wishlistDao;


    @Override
    public WishlistResponse addProductToWishlist(String userId, AddProductToWishlist request) {
        String productId = request.getProductId();
        log.info("user id: {} add product: {} to wishlist", userId, productId);

        if (!productService.isProductExist(productId)) {
            log.info("product: {} not found", productId);
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        Wishlist wishlist = wishlistDao.findByUser(userId)
                .orElseGet(() -> Wishlist.builder()
                        .userId(userId)
                        .build());
        if (wishlist.containsProduct(productId)) {
            log.info("Product already added removing");
            wishlist.removeProduct(productId);
        } else {
            log.info("Product added");
            wishlist.addProduct(productId);
        }

        Wishlist savedWishlist = wishlistDao.save(wishlist);
        log.info("Product added to wishlist successfully");
        return WishlistMapper.toWishlistResponse(savedWishlist);
    }


    @Override
    public WishlistResponse getWishlist(String userId) {

        Wishlist wishlist = wishlistDao.findByUser(userId)
                .orElseGet(() -> Wishlist.builder()
                        .userId(userId)
                        .build());

        Wishlist savedWishlist = wishlistDao.save(wishlist);
        return WishlistMapper.toWishlistResponse(savedWishlist);
    }

    @Override
    public WishlistResponse getWishlistById(Long id) {

        return WishlistMapper.toWishlistResponse(wishlistDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Wishlist not found with id: " + id)
        ));
    }

    @Override
    public PagedResponse<WishlistResponse> getAllWishlists(int page, int size, boolean isNewest) {

        Page<Wishlist> wishlists = wishlistDao.findAll(page, size, isNewest);

        return PageUtils.buildPagedResponse(wishlists, WishlistMapper::toWishlistResponse);
    }


}
