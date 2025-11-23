package com.vendor_marketplace.transaction_report_service.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SellerReport extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sellerId;

    @Builder.Default
    private Long totalEarnings = 0L;

    @Builder.Default
    private Long totalSales = 0L;

    @Builder.Default
    private Long totalRefunds = 0L;

    @Builder.Default
    private Long totalTax = 0L;

    @Builder.Default
    private Long netEarnings = 0L;

    @Builder.Default
    private Integer totalOrders = 0;

    @Builder.Default
    private Integer cancelOrders = 0;

    @Builder.Default
    private Integer totalTransactions = 0;

    public boolean isOlderThan30Days() {
        return getCreatedAt().isBefore(LocalDateTime.now().minusDays(30));
    }
}
