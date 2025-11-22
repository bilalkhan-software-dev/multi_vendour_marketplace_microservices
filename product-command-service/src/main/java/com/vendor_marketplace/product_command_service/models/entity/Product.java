package com.vendor_marketplace.product_command_service.models.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

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

    private String title;

    @Indexed(unique = true)
    private String productId;

    private String description;

    private Integer mrpPrice;

    private Integer sellingPrice;

    private Double discountInPercentage;

    @Builder.Default
    private int stocks = 0;

    private String colors;

    /**
     * Seller Business name
     */
    private String brand;


    @Builder.Default
    private List<String> images = new ArrayList<>();

    @DBRef
    private Category category;

    private String sellerId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String sizes;

}