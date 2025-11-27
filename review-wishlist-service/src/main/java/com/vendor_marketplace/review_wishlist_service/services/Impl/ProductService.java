package com.vendor_marketplace.review_wishlist_service.services.Impl;

import com.vendor_marketplace.review_wishlist_service.feignClient.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductServiceClient productServiceClient;

    public boolean isProductExist(String productId) {
        try {
            ResponseEntity<Boolean> response = productServiceClient.getProductExist(productId);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            log.warn("Failed to verify product existence for ID: {}", productId, e);
            return false;
        }
    }
}
