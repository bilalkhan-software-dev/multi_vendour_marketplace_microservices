package com.vendor_marketplace.review_wishlist_service.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductToWishlist {

    @NotBlank(message = "Product is required")
    private String productId;

}
