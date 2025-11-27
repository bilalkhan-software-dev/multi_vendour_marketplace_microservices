package com.vendor_marketplace.home_service.models.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Home {


    @Builder.Default
    private List<HomeCategoryResponse> shopByCategory = new ArrayList<>();

    @Builder.Default
    private List<HomeCategoryResponse> grid = new ArrayList<>();

    @Builder.Default
    private List<HomeCategoryResponse> electronicCategories = new ArrayList<>();

}

