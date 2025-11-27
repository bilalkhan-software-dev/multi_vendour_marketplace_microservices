package com.vendor_marketplace.home_service.models.dto.request;

import com.vendor_marketplace.home_service.models.entity.enums.HomeCategorySection;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateHomeCategoryRequest {

    private String categoryId;
    private String name;
    private String image;
    private HomeCategorySection homeCategorySection;


}
