package com.vendor_marketplace.review_wishlist_service.controller;


import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.review_wishlist_service.handler.GenericResponseHandler;
import com.vendor_marketplace.review_wishlist_service.models.dto.response.ReviewResponse;
import com.vendor_marketplace.review_wishlist_service.services.ReviewService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/review/public")
public class ReviewPublicController {

    private final ReviewService reviewService;
    private final GenericResponseHandler response;

    @GetMapping
    ResponseEntity<?> getProductReviews(
            @RequestParam @NotBlank(message = "Product ID is required") String productId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "30") int size,
            @RequestParam(required = false, defaultValue = "true") boolean isNewest
    ) {

        PagedResponse<ReviewResponse> productReviews = reviewService.getProductReviews(productId, page, size, isNewest);

        return response.createBuildResponse("Product reviews fetched successfully!", productReviews, HttpStatus.OK);
    }



}
