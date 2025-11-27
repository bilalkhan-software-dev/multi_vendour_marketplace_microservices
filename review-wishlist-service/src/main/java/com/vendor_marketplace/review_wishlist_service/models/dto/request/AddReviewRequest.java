package com.vendor_marketplace.review_wishlist_service.models.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class AddReviewRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotBlank(message = "Please write something about the product")
    @Size(max = 200, message = "Comment cannot exceed 200 characters")
    private String comment;

    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Builder.Default
    @Size(max = 5, message = "Maximum 5 images allowed")
    private List<String> images = new ArrayList<>();
}