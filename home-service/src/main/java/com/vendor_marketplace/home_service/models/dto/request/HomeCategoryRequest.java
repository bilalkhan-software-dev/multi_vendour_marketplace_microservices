package com.vendor_marketplace.home_service.models.dto.request;

import com.vendor_marketplace.home_service.models.entity.enums.HomeCategorySection;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HomeCategoryRequest {

    @NotBlank(message = "Category Id is required")
    private String categoryId;

    @NotBlank(message = "Category Name is required")
    private String name;

    @NotBlank(message = "Category Image is required")
    private String image;

    @NotBlank(message = "Category Section is required")
    private HomeCategorySection homeCategorySection;


}
