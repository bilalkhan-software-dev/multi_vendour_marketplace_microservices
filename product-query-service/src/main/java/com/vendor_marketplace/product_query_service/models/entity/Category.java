package com.vendor_marketplace.product_query_service.models.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Document(collection = "categories")
public class Category {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String categoryId;

    @DBRef
    private Category parentCategory;

    private Integer level;

}