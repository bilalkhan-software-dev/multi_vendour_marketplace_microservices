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
public class Wishlist extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private List<String> products = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String userId;


}
