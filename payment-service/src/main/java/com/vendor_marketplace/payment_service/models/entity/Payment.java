package com.vendor_marketplace.payment_service.models.entity;

import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment_tbl", indexes = {
        @Index(name = "idx_payment_user_id", columnList = "user_id"),
        @Index(name = "idx_payment_order_id", columnList = "order_id"),
        @Index(name = "idx_payment_created_at", columnList = "created_at")
})
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    private Long totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private String paymentMethod;

    /**
     * Payment id for stripe/ Jazz cash
     */
    @Column(unique = true)
    private String paymentLinkId;

    @ElementCollection
    @CollectionTable(
            name = "payment_seller_ids",
            joinColumns = @JoinColumn(name = "payment_id")
    )
    @Column(name = "seller_id")
    @Builder.Default
    private List<String> sellerIds = new ArrayList<>();

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String userEmail;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
