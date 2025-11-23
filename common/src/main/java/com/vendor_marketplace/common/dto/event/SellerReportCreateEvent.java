package com.vendor_marketplace.common.dto.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerReportCreateEvent {


    private String sellerId;
    
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


}
