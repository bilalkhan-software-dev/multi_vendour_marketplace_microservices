package com.vendor_marketplace.home_service.services;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.home_service.models.dto.request.HomeCategoryRequest;
import com.vendor_marketplace.home_service.models.dto.request.UpdateHomeCategoryRequest;
import com.vendor_marketplace.home_service.models.dto.response.Home;
import com.vendor_marketplace.home_service.models.dto.response.HomeCategoryResponse;
import com.vendor_marketplace.home_service.models.entity.enums.HomeCategorySection;

import java.util.List;

public interface HomeService {
    HomeCategoryResponse createHomeCategory(HomeCategoryRequest homeCategoryRequest);

    HomeCategoryResponse updateHomeCategory(String homeCategoryId, UpdateHomeCategoryRequest req);

    void deleteHomeCategory(String homeCategoryId);

    Home createHomeCategories(List<HomeCategoryRequest> homeCategoryRequests);

    Home getHomeCategories();

    PagedResponse<HomeCategoryResponse> getHomeCategoriesBySection(HomeCategorySection section, int page, int size, boolean isNewest);

    PagedResponse<HomeCategoryResponse> getAllHomeCategories(int page, int size, boolean isNewest);
}
