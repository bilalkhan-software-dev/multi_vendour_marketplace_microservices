package com.vendor_marketplace.review_wishlist_service.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    @Column(nullable = false, unique = true)
    private String userId;

    @ElementCollection
    @CollectionTable(
            name = "wishlist_products",
            joinColumns = @JoinColumn(name = "wishlist_id")
    )
    @Column(name = "product_id")
    @Builder.Default
    private Set<String> productIds = new HashSet<>();

    public void addProduct(@NonNull String productId) {
        productIds.add(productId);
    }

    public void removeProduct(@NonNull String productId) {
        productIds.remove(productId);
    }

    public boolean containsProduct(@NonNull String productId) {
        return productIds.contains(productId);
    }


}
