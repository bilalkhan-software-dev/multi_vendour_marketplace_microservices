package com.vendor_marketplace.seller_service.models.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vendor_marketplace.seller_service.models.entity.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "sellers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String keyCloakId;

    private String name;
    private String mobile;

    @Column(unique = true,nullable = false)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Embedded
    private BankDetails bankDetails = new BankDetails();

    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    @OneToOne(cascade = CascadeType.ALL)
    private SellerAddress pickupAddress;

    private String STRN;
    /**
     * STRN (Sales Tax Registration Number):
     * Issued by the Federal Board of Revenue (FBR) to businesses that are registered for Sales Tax.
     * Required for businesses that make taxable supplies and must charge sales tax.
     */


    @Builder.Default
    private String role = "ROLE_SELLER";

    @Builder.Default
    private boolean isEmailVerified =  false;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;




}
