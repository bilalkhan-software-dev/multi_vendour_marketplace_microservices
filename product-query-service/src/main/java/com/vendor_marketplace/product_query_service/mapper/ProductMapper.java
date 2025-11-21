package com.vendor_marketplace.product_query_service.mapper;

import com.vendor_marketplace.common.dto.response.ProductResponse;
import com.vendor_marketplace.product_query_service.models.entity.Category;
import com.vendor_marketplace.product_query_service.models.entity.Product;

public class ProductMapper {
    public static ProductResponse toProductResponse(Product product) {
        if (product == null) return null;

        return ProductResponse.builder()
                .productId(product.getProductId())
                .title(product.getTitle())
                .description(product.getDescription())
                .mrpPrice(product.getMrpPrice())
                .sellingPrice(product.getSellingPrice())
                .stocks(product.getStocks())
                .discountInPercentage(product.getDiscountInPercentage())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .images(product.getImages())
                .color(product.getColors())
                .sizes(product.getSizes())
                .brand(product.getBrand())
                .sellerId(product.getSellerId())
                .category(mapToCategory(product.getCategory()))
                .build();
    }


    public static ProductResponse.ProductCategory mapToCategory(Category category) {
        if (category == null) {
            return null; // stop condition if category null
        }
        return ProductResponse.ProductCategory.builder()
                .id(category.getId())
                .name(category.getName())
                .level(category.getLevel())
                .categoryId(category.getCategoryId())
                .parentCategory(mapToCategory(category.getParentCategory()))
                .build();
    }
}
