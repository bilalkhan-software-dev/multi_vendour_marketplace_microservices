package com.vendor_marketplace.review_wishlist_service.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddProductToWishlist {

    @NotBlank(message = "Product is required")
    private String productId;

}
