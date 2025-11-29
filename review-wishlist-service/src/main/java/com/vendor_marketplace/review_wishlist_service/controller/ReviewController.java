package com.vendor_marketplace.review_wishlist_service.controller;


import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.review_wishlist_service.handler.GenericResponseHandler;
import com.vendor_marketplace.review_wishlist_service.models.dto.request.AddReviewRequest;
import com.vendor_marketplace.review_wishlist_service.models.dto.request.UpdateReviewRequest;
import com.vendor_marketplace.review_wishlist_service.models.dto.response.ReviewResponse;
import com.vendor_marketplace.review_wishlist_service.services.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.vendor_marketplace.common.constants.AuthHeaderConstant.CUSTOM_USER_ID_AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/reviews/user")
public class ReviewController {

    private final ReviewService reviewService;
    private final GenericResponseHandler response;

    @PostMapping("/add")
    ResponseEntity<?> addReview(@RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId, @Valid @RequestBody AddReviewRequest request) {

        ReviewResponse added = reviewService.addReview(request, userId);

        return response.createBuildResponse("Review added successfully!", added, HttpStatus.CREATED);

    }


    @PatchMapping("/{id}")
    ResponseEntity<?> updateReview(@RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
                                   @PathVariable @NotNull(message = "Review Id is required") Long id,
                                   @Valid @RequestBody UpdateReviewRequest request) {

        ReviewResponse added = reviewService.updateReview(id, userId, request);

        return response.createBuildResponse("Review updated successfully!", added, HttpStatus.OK);

    }



    @GetMapping("/")
    ResponseEntity<?> getUserReviews(
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "30") int size,
            @RequestParam(required = false, defaultValue = "true") boolean isNewest
    ) {


        PagedResponse<ReviewResponse> productReviews = reviewService.getUserReviews(userId, page, size, isNewest);

        return response.createBuildResponse("Your reviews fetched successfully!", productReviews, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getReviewById(@PathVariable @NotNull(message = "Review Id is required") Long id) {

        ReviewResponse review = reviewService.getById(id);

        return response.createBuildResponse("Review detail retrieved successfully!", review, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteReviewById(
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
            @PathVariable @NotNull(message = "Review Id is required") Long id) {

        reviewService.deleteReview(userId, id);

        return response.createBuildResponseMessage("Review deleted successfully!", HttpStatus.OK);
    }


}
