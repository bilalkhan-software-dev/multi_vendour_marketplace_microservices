package com.vendor_marketplace.review_wishlist_service.models.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ReviewResponse {

    public Long id;

    private String userId;
    private String productId;
    private String comment;

    private Integer rating;

    @Builder.Default
    private List<String> images = new ArrayList<>();


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

