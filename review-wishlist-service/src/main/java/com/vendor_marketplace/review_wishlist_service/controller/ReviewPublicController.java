package com.vendor_marketplace.review_wishlist_service.controller;


import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.review_wishlist_service.handler.GenericResponseHandler;
import com.vendor_marketplace.review_wishlist_service.models.dto.response.ReviewResponse;
import com.vendor_marketplace.review_wishlist_service.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v2/reviews/public")
@Tag(
        name = "Public Reviews",
        description = "Public endpoints for viewing product reviews"
)
public class ReviewPublicController {

    private final ReviewService reviewService;
    private final GenericResponseHandler response;

    @Operation(
            summary = "Get product reviews",
            description = "Retrieve paginated reviews for a specific product"
    )
    @GetMapping()
    ResponseEntity<?> getProductReviews(
            @RequestParam @NotBlank(message = "Product ID is required") String productId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "30") int size,
            @RequestParam(required = false, defaultValue = "true") boolean isNewest
    ) {

        PagedResponse<ReviewResponse> productReviews = reviewService.getProductReviews(productId, page, size, isNewest);

        if (productReviews.getContent().isEmpty()) {
            return response.createBuildResponse("This product currently has no reviews", productReviews, HttpStatus.OK);
        }


        return response.createBuildResponse("Product reviews fetched successfully!", productReviews, HttpStatus.OK);
    }


}
