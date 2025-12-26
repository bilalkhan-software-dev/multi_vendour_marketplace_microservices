package com.vendor_marketplace.auth_service.models.entity;

import com.vendor_marketplace.common.dto.enums.AccountStatus;
import com.vendor_marketplace.common.dto.enums.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "auth_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String fullName;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(unique = true)
    private String salesTaxRegistrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private USER_ROLE role;


    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
