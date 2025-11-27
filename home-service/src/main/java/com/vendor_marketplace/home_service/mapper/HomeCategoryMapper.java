package com.vendor_marketplace.home_service.mapper;

import com.vendor_marketplace.home_service.models.dto.response.HomeCategoryResponse;
import com.vendor_marketplace.home_service.models.entity.HomeCategory;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HomeCategoryMapper {

    public HomeCategoryResponse  toHomeCategoryResponse(HomeCategory homeCategory) {
        return HomeCategoryResponse.builder()
                .id(homeCategory.getId())
                .name(homeCategory.getName())
                .categoryId(homeCategory.getCategoryId())
                .homeCategorySection(homeCategory.getHomeCategorySection())
                .createdAt(homeCategory.getCreatedAt())
                .updatedAt(homeCategory.getUpdatedAt())
                .build();
    }


}
