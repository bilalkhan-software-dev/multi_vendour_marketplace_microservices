package com.vendor_marketplace.review_wishlist_service.models.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Review extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String userId;

    @Column(length = 200)
    private String comment; // description

    private Integer rating;

    @Builder.Default
    @ElementCollection
    private List<String> images = new ArrayList<>(); // Optional product images






}
