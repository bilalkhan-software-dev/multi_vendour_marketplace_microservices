package com.vendor_marketplace.review_wishlist_service.controller;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.review_wishlist_service.handler.GenericResponseHandler;
import com.vendor_marketplace.review_wishlist_service.models.dto.response.ReviewResponse;
import com.vendor_marketplace.review_wishlist_service.models.dto.response.WishlistResponse;
import com.vendor_marketplace.review_wishlist_service.services.ReviewService;
import com.vendor_marketplace.review_wishlist_service.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/rw")
public class AdminReviewWishlistController {


    private final ReviewService reviewService;
    private final WishlistService wishlistService;
    private final GenericResponseHandler response;


    @GetMapping("/reviews")
    ResponseEntity<?> getAllReviews(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "30") int size,
            @RequestParam(required = false, defaultValue = "true") Boolean isNewest
    ) {

        PagedResponse<ReviewResponse> productReviews = reviewService.getAllReviews(page, size, isNewest);

        return response.createBuildResponse("Reviews fetched successfully!", productReviews, HttpStatus.OK);
    }

    @GetMapping("/wishlists")
    ResponseEntity<?> getAllWishlists(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "30") int size,
            @RequestParam(required = false, defaultValue = "true") Boolean isNewest
    ) {

        PagedResponse<WishlistResponse> productReviews = wishlistService.getAllWishlists(page, size, isNewest);

        return response.createBuildResponse("Wishlists fetched successfully!", productReviews, HttpStatus.OK);
    }


}
