package com.vendor_marketplace.transaction_report_service.models.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SellerReportResponse {

    
    private Long id;
    private String sellerId;

    @Builder.Default
    private Long totalEarnings=0L;
    
    @Builder.Default
    private Long totalSales=0L;

    @Builder.Default
    private Long totalRefunds=0L;

    @Builder.Default
    private Long totalTax=0L;

    @Builder.Default
    private Long netEarnings=0L;

    @Builder.Default
    private Integer totalOrders=0;

    @Builder.Default
    private Integer cancelOrders=0;

    @Builder.Default
    private Integer totalTransactions=0;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
