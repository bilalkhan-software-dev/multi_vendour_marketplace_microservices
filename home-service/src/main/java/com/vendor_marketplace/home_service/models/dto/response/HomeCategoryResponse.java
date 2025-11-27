package com.vendor_marketplace.home_service.models.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HomeCategoryResponse {

    private String id;
    private String categoryId;
    private String name;
    private String image;
    private String homeCategorySection;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
