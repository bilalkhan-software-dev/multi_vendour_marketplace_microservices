package com.vendor_marketplace.review_wishlist_service.services.Impl;

import com.vendor_marketplace.review_wishlist_service.feignClient.ProductServiceClient;
import com.vendor_marketplace.review_wishlist_service.services.ReviewService;
import com.vendor_marketplace.review_wishlist_service.services.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class WishlistServiceImpl implements WishlistService {

    private final ProductServiceClient productService;

}
