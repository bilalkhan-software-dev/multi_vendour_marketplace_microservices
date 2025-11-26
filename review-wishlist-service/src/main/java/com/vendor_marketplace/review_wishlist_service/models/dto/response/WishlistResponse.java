package com.vendor_marketplace.review_wishlist_service.models.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class WishlistResponse {

    private Long id;
    private String userId;

    @Builder.Default
    private List<String> products = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
