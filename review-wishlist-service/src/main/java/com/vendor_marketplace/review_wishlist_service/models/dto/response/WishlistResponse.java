package com.vendor_marketplace.review_wishlist_service.models.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class WishlistResponse {

    private Long id;
    private String userId;

    @Builder.Default
    private Set<String> products = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
