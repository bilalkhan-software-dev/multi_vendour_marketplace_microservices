package com.vendor_marketplace.cart_service.models.entity;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cartId;

    @Column(nullable = false)
    private String productId;

    private int quantity = 1;
    private Integer mrpPrice;
    private Integer sellingPrice;

    @Column(nullable = false)
    private String userId;
}
