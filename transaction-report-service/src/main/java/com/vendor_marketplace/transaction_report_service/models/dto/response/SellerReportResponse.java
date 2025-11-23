package com.vendor_marketplace.transaction_report_service.models.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SellerReportResponse {

    private Long id;
    private String sellerId;
    private Long totalEarnings;
    private Long totalSales;
    private Long totalRefunds;
    private Long totalTax;
    private Long netEarnings;
    private Integer totalOrders;
    private Integer cancelOrders;
    private Integer totalTransactions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
