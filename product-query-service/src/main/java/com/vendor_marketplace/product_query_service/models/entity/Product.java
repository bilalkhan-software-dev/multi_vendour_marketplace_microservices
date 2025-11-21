package com.vendor_marketplace.product_query_service.models.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @Indexed(unique = true)
    private String productId;

    @Indexed
    private String title;

    @Indexed
    private String description;

    private Integer mrpPrice;

    @Indexed
    private Integer sellingPrice;

    private Double discountInPercentage;

    @Builder.Default
    private int stocks = 0;

    private List<String> colors;

    /**
     * Seller Business name
     */
    private String brand;

    @Builder.Default
    private List<String> images = new ArrayList<>();

    @DBRef
    private Category category;

    @Indexed
    private String sellerId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private List<String> sizes;


}