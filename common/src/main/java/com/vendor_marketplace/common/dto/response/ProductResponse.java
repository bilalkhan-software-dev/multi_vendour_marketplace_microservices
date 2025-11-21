package com.vendor_marketplace.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private String productId;
    private String title;
    private String description;
    private String brand;
    private String sellerId;

    private Integer mrpPrice;
    private Integer sellingPrice;
    private Integer stocks;
    private Double discountInPercentage;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<String> images = new ArrayList<>();
    private List<String> color = new ArrayList<>();
    private List<String> sizes = new ArrayList<>();


    private ProductCategory category;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductCategory {
        private String id;
        private String name;
        private Integer level;
        private String categoryId;
        private ProductCategory parentCategory;
    }
}
