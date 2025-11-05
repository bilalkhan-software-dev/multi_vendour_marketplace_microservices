package com.vendor_marketplace.seller_service.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "seller_addresses")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String addressName; // "Main Warehouse", "Store Front", "Pickup Point"

    private String locality;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    // Seller relationship
    private Long sellerId;

    private String mobile;
    private String address;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}

