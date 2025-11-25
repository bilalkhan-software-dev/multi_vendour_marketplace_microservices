package com.vendor_marketplace.common.dto.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerReportCreateEvent {

    private String sellerId;           // Required: Identifies which seller this report is for

    private Long totalEarnings = 0L;   // Total revenue from successful orders (after refunds)
    private Long totalRefunds = 0L;    // Total amount refunded to customers
    private Long totalTax = 0L;        // Total tax collected
    private Long netEarnings = 0L;     // Final earnings after all deductions (tax, refunds, etc.)

    private Long totalSales = 0L;      // Total sales value (before any deductions)
    private Integer totalOrders = 0;   // Count of all orders placed
    private Integer cancelOrders = 0;  // Count of cancelled orders
    private Integer totalTransactions = 0; // Count of successful payments/transactions


}
